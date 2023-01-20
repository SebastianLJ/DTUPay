package org.dtu.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.cqrs.CorrelationID;
import messageUtilities.cqrs.events.Event;

import java.util.UUID;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CustomerAccountDeleted extends Event {
    UUID customerID;

    public CustomerAccountDeleted(CorrelationID correlationID, UUID customerID) {
        super(correlationID);
        this.customerID = customerID;
    }
}
