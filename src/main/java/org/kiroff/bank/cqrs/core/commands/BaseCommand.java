package org.kiroff.bank.cqrs.core.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.kiroff.bank.cqrs.core.messages.Message;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public abstract class BaseCommand extends Message {

}
