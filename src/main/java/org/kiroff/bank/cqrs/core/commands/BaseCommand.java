package org.kiroff.bank.sqrs.core.commands;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.kiroff.bank.sqrs.core.messages.Message;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseCommand extends Message
{
    public BaseCommand(String id)
    {
        super(id);
    }
}
