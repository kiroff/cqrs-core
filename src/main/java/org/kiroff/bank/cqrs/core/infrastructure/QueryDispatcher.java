package org.kiroff.bank.cqrs.core.infrastructure;

import org.kiroff.bank.cqrs.core.domain.BaseEntity;
import org.kiroff.bank.cqrs.core.queries.BaseQuery;
import org.kiroff.bank.cqrs.core.queries.QueryHandlerMethod;

import java.util.List;

public interface QueryDispatcher {
    <T extends BaseQuery> void registerHandler(Class<T> type, QueryHandlerMethod<T> queryHandlerMethod);

    <U extends BaseEntity> List<U> send(BaseQuery query);
}
