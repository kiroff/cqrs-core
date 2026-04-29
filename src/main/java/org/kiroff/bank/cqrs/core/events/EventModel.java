package org.kiroff.bank.cqrs.core.events;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Data
@Builder
@Document(collection = "event-store")
public class EventModel {
    @Id
    private String id;
    private int version;
    private LocalDateTime timeStamp;
    private String aggregateIdentifier;
    private String aggregateType;
    private String eventType;
    @Field("eventData")
    private Object rawEventData;

    public BaseEvent asEventData() {
        if (rawEventData == null) {
            return null;
        }
        if (rawEventData instanceof BaseEvent baseEvent) {
            return baseEvent;
        }

        var eventClass = resolveEventClass();
        if (eventClass == null) {
            throw new IllegalStateException("Cannot resolve event type for " + eventType);
        }

        if (eventClass.isInstance(rawEventData)) {
            return eventClass.cast(rawEventData);
        }
        if (rawEventData instanceof Map<?, ?> values) {
            return instantiateEvent(eventClass, values);
        }

        throw new IllegalStateException(
                "Unsupported event payload type " + rawEventData.getClass().getName() + " for " + eventType
        );
    }

    private Class<? extends BaseEvent> resolveEventClass() {
        var candidateName = eventType.contains(".")
                ? eventType
                : "org.kiroff.bank.account.common.events." + eventType;
        try {
            var resolved = Class.forName(candidateName);
            if (!BaseEvent.class.isAssignableFrom(resolved)) {
                return null;
            }
            return resolved.asSubclass(BaseEvent.class);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    private BaseEvent instantiateEvent(Class<? extends BaseEvent> eventClass, Map<?, ?> values) {
        try {
            Constructor<? extends BaseEvent> constructor = eventClass.getDeclaredConstructor();
            if (!Modifier.isPublic(constructor.getModifiers()) || !Modifier.isPublic(eventClass.getModifiers())) {
                constructor.setAccessible(true);
            }
            BaseEvent event = constructor.newInstance();
            values.forEach((name, value) -> setField(event, String.valueOf(name), value));
            return event;
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Failed to instantiate event type " + eventClass.getName(), ex);
        }
    }

    private void setField(BaseEvent target, String fieldName, Object value) {
        Class<?> currentType = target.getClass();
        while (currentType != null) {
            try {
                var field = currentType.getDeclaredField(fieldName);
                if (!field.canAccess(target)) {
                    field.setAccessible(true);
                }
                field.set(target, convertValue(value, field.getType()));
                return;
            } catch (NoSuchFieldException ignored) {
                currentType = currentType.getSuperclass();
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException(
                        "Failed to map field '" + fieldName + "' for event type " + target.getClass().getName(),
                        ex
                );
            }
        }
    }

    private Object convertValue(Object value, Class<?> targetType) {
        if (value == null || targetType.isInstance(value)) {
            return value;
        }
        if (targetType == String.class) {
            return String.valueOf(value);
        }
        if (targetType == int.class || targetType == Integer.class) {
            return ((Number) value).intValue();
        }
        if (targetType == long.class || targetType == Long.class) {
            return ((Number) value).longValue();
        }
        if (targetType == double.class || targetType == Double.class) {
            return ((Number) value).doubleValue();
        }
        if (targetType == float.class || targetType == Float.class) {
            return ((Number) value).floatValue();
        }
        if (targetType == boolean.class || targetType == Boolean.class) {
            return value instanceof Boolean bool ? bool : Boolean.parseBoolean(value.toString());
        }
        if (targetType == LocalDate.class) {
            if (value instanceof LocalDate localDate) {
                return localDate;
            }
            if (value instanceof java.util.Date date) {
                return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
            return LocalDate.parse(value.toString());
        }
        if (targetType.isEnum()) {
            @SuppressWarnings({"rawtypes", "unchecked"})
            Class<? extends Enum> enumType = (Class<? extends Enum>) targetType;
            return Enum.valueOf(enumType, value.toString());
        }
        return value;
    }
}
