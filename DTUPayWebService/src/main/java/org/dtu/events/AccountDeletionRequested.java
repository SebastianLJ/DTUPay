package org.dtu.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;
import org.dtu.aggregate.User;

import java.util.UUID;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AccountDeletionRequested extends Event {
    User user;

    public AccountDeletionRequested(CorrelationID correlationID, User user) {
        super(correlationID);
        this.user = user;
    }
}
