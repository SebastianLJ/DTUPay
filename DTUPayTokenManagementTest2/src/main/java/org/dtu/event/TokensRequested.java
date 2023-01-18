package org.dtu.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import org.dtu.messageUtilities.CorrelationID;
import org.dtu.messageUtilities.cqrs.events.Event;
import org.dtu.aggregate.UserId;

@Value
@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
public class TokensRequested extends Event {

    private static final long serialVersionUID = -8094840615477468312L;

    int amount;
    UserId userId;

    public TokensRequested(CorrelationID correlationID, int amount, UserId userId) {
        super(correlationID);
        this.amount = amount;
        this.userId = userId;
    }
}
