package org.dtu.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.events.Event;

import java.util.UUID;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PaymentRequested extends Event {

    private UUID paymentId;
    private UUID token;
    private UUID merchantId;
    private int amount;
}
