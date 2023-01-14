package org.dtu.events;

import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;

import java.util.UUID;

@Value
@EqualsAndHashCode(callSuper = false)
public class PaymentRequested extends Event {

    UUID paymentId;
    UUID token;
    UUID merchantId;
    int amount;

    public PaymentRequested(CorrelationID correlationID, UUID paymentId, UUID token, UUID merchantId, int amount) {
        super(correlationID);
        this.paymentId = paymentId;
        this.token = token;
        this.merchantId = merchantId;
        this.amount = amount;
    }
}
