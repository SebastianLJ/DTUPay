package org.dtu.events;

import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;

@Value
@EqualsAndHashCode(callSuper = false)
public class CustomerBankNumberAssigned extends Event {
    String bankNumber;

    public CustomerBankNumberAssigned(CorrelationID correlationID, String bankNumber) {
        super(correlationID);
        this.bankNumber = bankNumber;
    }
}
