package org.dtu.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.cqrs.events.Event;
import org.dtu.aggregate.Payment;
import org.dtu.aggregate.UserId;

import java.util.List;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MerchantReportGenerated extends Event {

    private static final long serialVersionUID = -2526892406205515042L;

    UserId merchantId;
    List<Payment> payments;
}
