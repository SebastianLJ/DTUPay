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
public class CustomerReportGenerated extends Event {

    private static final long serialVersionUID = 1781588122418892194L;

    UserId customerId;
    List<Payment> payments;
}
