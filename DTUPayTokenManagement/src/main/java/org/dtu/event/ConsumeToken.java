package org.dtu.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.cqrs.events.Event;
import org.dtu.aggregate.Token;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ConsumeToken extends Event {

    Token token;
}
