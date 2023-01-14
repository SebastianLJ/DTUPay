package org.dtu.events;

import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;

@Value
@EqualsAndHashCode(callSuper = false)
public class MoneyTransferred extends Event {

    public MoneyTransferred(CorrelationID correlationID) {
        super(correlationID);
    }
}
