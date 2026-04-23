package org.kiroff.bank.cqrs.core.handlers;

import org.kiroff.bank.cqrs.core.domain.AggregateRoot;

public interface EventSourcingHandler<T> {

    void save(AggregateRoot aggregate);

    T findById(String id);

    void republishEvents();
}
