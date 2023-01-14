package org.dtu.events;

import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;
import org.dtu.aggregate.UserId;

@Value
@EqualsAndHashCode(callSuper = false)
public class TokenRequested extends Event {
    private static final long serialVersionUID = 1596683920706802940L;
    int amount;
    UserId userId;

    public TokenRequested(CorrelationID correlationID, int amount, UserId userId) {
        super(correlationID);
        this.amount = amount;
        this.userId = userId;
    }
}
