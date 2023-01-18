package org.dtu.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.dtu.messageUtilities.CorrelationID;
import org.dtu.messageUtilities.cqrs.events.Event;
/*import org.dtu.aggregate.Token;*/
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
