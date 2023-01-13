package org.dtu.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.events.Event;

import java.util.UUID;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AccountDeletionRequested extends Event {
    private UUID customerID;
}
