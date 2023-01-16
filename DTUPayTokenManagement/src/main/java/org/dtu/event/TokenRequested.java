package org.dtu.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;
import org.dtu.aggregate.UserId;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TokenRequested extends Event {

    int amount;
    UserId userId;

    public TokenRequested(CorrelationID correlationID, int amount, UserId userId) {
        super(correlationID);
        this.amount = amount;
        this.userId = userId;
    }
}
