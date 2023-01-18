package org.dtu.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;
import org.dtu.aggregate.UserId;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TokenConsumed extends Event {

    UserId userId;

    public TokenConsumed(CorrelationID correlationID, UserId userId) {
        super(correlationID);
        this.userId = userId;
    }
}

