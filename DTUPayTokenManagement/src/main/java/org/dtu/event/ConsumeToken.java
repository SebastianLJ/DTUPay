package org.dtu.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.events.Event;
import org.dtu.aggregate.Token;

@Value
@AllArgsConstructor
@EqualsAndHashCode
public class ConsumeToken extends Event {

    private Token token;
}
