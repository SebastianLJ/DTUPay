package org.dtu.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.cqrs.events.Event;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MerchantBankNumberAssigned extends Event {
    private String bankNumber;
}
