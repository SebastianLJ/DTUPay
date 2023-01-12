package org.dtu.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.events.Event;
import org.dtu.aggregate.User;
import org.dtu.aggregate.UserId;
@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GenerateToken extends Event {

    int amount;
    UserId userId;

}
