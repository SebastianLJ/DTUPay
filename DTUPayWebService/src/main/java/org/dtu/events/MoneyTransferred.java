package org.dtu.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.events.Event;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MoneyTransferred extends Event {

}
