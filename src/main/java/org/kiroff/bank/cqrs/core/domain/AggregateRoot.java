package org.kiroff.bank.cqrs.core.domain;

import lombok.Getter;
import lombok.Setter;
import org.kiroff.bank.cqrs.core.events.BaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot {
    private static final Logger LOGGER = LoggerFactory.getLogger(AggregateRoot.class);
    private final List<BaseEvent> changes = new ArrayList<>();
    @Getter
    protected String id;
    @Getter
    @Setter
    private int version = -1;

    public List<BaseEvent> getUncommittedChanges() {
        return this.changes;
    }

    public void markChangesAsCommitted() {
        this.changes.clear();
    }

    protected void applyChange(BaseEvent event, boolean isNewEvent) {
        invokeApply(event);
        if (isNewEvent) {
            this.changes.add(event);
        }
    }

    private void invokeApply(BaseEvent event) {
        Class<?> eventType = event.getClass();
        while (eventType != null) {
            try {
                Method applyMethod = getClass().getMethod("apply", eventType);
                applyMethod.invoke(this, event);
                return;
            } catch (NoSuchMethodException ignored) {
                eventType = eventType.getSuperclass();
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new IllegalStateException("Failed to apply event " + event.getClass().getName(), ex);
            }
        }

        LOGGER.warn("No apply method found for event type {}", event.getClass().getName());
    }

    public void raiseEvent(BaseEvent event) {
        applyChange(event, true);
    }

    public void replayEvents(Iterable<BaseEvent> events) {
        events.forEach(e -> applyChange(e, false));
    }
}
