package org.dtu.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;
import org.dtu.aggregate.User;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TokensDeleted extends Event {

    User user;

    public TokensDeleted(CorrelationID correlationID, User user) {
        super(correlationID);
        this.user = user;
    }
}
