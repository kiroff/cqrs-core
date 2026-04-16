package org.kiroff.bank.cqrs.core.events;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

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
    private BaseEvent eventData;
}
