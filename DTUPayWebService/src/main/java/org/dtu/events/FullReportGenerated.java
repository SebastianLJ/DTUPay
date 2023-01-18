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
public class FullReportGenerated extends Event {
    private static final long serialVersionUID = -7558442089500047644L;
    List<Payment> payments;
}
