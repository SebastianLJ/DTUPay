package org.dtu.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.events.Event;
import org.dtu.aggregate.UserId;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TokenRequested extends Event {

    int amount;
    UserId userId;

}
