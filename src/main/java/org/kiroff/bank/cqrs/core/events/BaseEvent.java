package org.kiroff.bank.cqrs.core.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.kiroff.bank.cqrs.core.messages.Message;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEvent extends Message
{

}
