package org.dtu.events;

import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;

import java.util.UUID;

@Value
@EqualsAndHashCode(callSuper = false)
public class AccountDeletionRequested extends Event {
    UUID customerID;

    public AccountDeletionRequested(CorrelationID correlationID, UUID customerID) {
        super(correlationID);
        this.customerID = customerID;
    }
}
