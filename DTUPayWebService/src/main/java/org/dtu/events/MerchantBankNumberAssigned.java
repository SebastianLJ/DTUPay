package org.dtu.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MerchantBankNumberAssigned extends Event {
    String bankNumber;

    public MerchantBankNumberAssigned(CorrelationID correlationID, String bankNumber) {
        super(correlationID);
        this.bankNumber = bankNumber;
    }
}
