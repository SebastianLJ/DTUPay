package org.dtu.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.cqrs.CorrelationID;
import messageUtilities.cqrs.events.Event;
import org.dtu.aggregate.User;


@Value
@AllArgsConstructor
@EqualsAndHashCode
public class CustomerAccountCreated extends Event {
    User user;

    public CustomerAccountCreated(CorrelationID correlationID, User user) {
        super(correlationID);
        this.user = user;
    }
}
