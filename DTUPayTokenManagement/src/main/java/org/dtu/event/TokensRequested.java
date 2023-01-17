package org.dtu.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;
import org.dtu.aggregate.UserId;

@Value
@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
public class TokensRequested extends Event {

    int amount;
    UserId userId;

    public TokensRequested(CorrelationID correlationID, int amount, UserId userId) {
        super(correlationID);
        this.amount = amount;
        this.userId = userId;
    }
}
