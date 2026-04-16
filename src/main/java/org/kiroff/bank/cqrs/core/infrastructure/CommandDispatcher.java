package org.kiroff.bank.cqrs.core.infrastructure;

import org.kiroff.bank.cqrs.core.commands.BaseCommand;
import org.kiroff.bank.cqrs.core.commands.CommandHandlerMethod;

public interface CommandDispatcher {
    <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> commandHandlerMethod);

    void send(BaseCommand command);
}
