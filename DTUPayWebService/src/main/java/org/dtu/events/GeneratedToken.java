package org.dtu.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.cqrs.events.Event;
import org.dtu.aggregate.Token;

import java.util.ArrayList;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GeneratedToken extends Event {
    private static final long serialVersionUID = -1599019626118724482L;

    ArrayList<Token> tokens;
}