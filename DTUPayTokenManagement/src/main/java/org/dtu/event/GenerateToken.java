package org.dtu.event;

import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;
import org.dtu.aggregate.UserId;

@Value
@EqualsAndHashCode(callSuper = false)
public class GenerateToken extends Event {

    int amount;
    UserId userId;

    public GenerateToken(CorrelationID correlationID, int amount, UserId userId) {
        super(correlationID);
        this.amount = amount;
        this.userId = userId;
    }
}
