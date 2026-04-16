package org.kiroff.bank.cqrs.core.excpetions;

public class AggregateNotFoundException extends RuntimeException
{
    public AggregateNotFoundException(String message)
    {
        super(message);
    }
}
