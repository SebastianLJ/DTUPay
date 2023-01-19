package org.dtu.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.cqrs.CorrelationID;
import messageUtilities.cqrs.events.Event;
import org.dtu.domain.Token;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ConsumeToken extends Event {

    Token token;

    public ConsumeToken(CorrelationID correlationID, Token token) {
        super(correlationID);
        this.token = token;
    }
}
