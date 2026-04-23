package org.kiroff.bank.cqrs.core.infrastructure;

import org.kiroff.bank.cqrs.core.events.BaseEvent;

import java.util.List;

public interface EventStore {
    void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion);

    List<BaseEvent> getEvents(String aggregateId);

    List<BaseEvent> getEventsForAllActiveAggregates();
}
