package org.dtu.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;

import java.util.UUID;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AccountDeletionRequested extends Event {
    UUID customerID;

    public AccountDeletionRequested(CorrelationID correlationID, UUID customerID) {
        super(correlationID);
        this.customerID = customerID;
    }
}
