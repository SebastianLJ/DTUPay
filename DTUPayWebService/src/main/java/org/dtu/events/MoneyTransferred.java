package org.dtu.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MoneyTransferred extends Event {

    public MoneyTransferred(CorrelationID correlationID) {
        super(correlationID);
    }
}
