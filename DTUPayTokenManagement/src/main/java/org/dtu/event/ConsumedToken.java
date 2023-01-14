package org.dtu.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;

import java.util.UUID;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ConsumedToken extends Event {

    UUID userId;

    public ConsumedToken(CorrelationID correlationID, UUID userId) {
        super(correlationID);
        this.userId = userId;
    }
}

