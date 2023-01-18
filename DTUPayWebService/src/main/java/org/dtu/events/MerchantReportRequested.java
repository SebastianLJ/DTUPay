package org.dtu.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.cqrs.events.Event;
import org.dtu.aggregate.UserId;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MerchantReportRequested extends Event {

    private static final long serialVersionUID = 883442431729178794L;

    UserId merchantId;
}
