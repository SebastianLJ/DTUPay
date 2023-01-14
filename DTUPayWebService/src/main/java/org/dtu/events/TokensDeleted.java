package org.dtu.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;

import java.util.UUID;

@Value
@EqualsAndHashCode(callSuper = false)
public class TokensDeleted extends Event {

    UUID customerID;

    public TokensDeleted(CorrelationID correlationID, UUID customerID) {
        super(correlationID);
        this.customerID = customerID;
    }
}
