package org.dtu.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.cqrs.CorrelationID;
import messageUtilities.cqrs.events.Event;
import org.dtu.aggregate.UserId;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserTokensRequested extends Event {
    private static final long serialVersionUID = 1097614630638811706L;
    UserId userId;

    public UserTokensRequested(CorrelationID correlationID, UserId userId) {
        super(correlationID);
        this.userId = userId;
    }
}
