package org.dtu.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.events.Event;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;

import java.util.ArrayList;
import java.util.UUID;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GeneratedToken extends Event {

    ArrayList<Token> tokens;
}