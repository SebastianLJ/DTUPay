package org.dtu.events;

import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;
import org.dtu.aggregate.Token;

import java.util.ArrayList;

@Value
@EqualsAndHashCode(callSuper = false)
public class GeneratedToken extends Event {
    private static final long serialVersionUID = -1599019626118724482L;

    ArrayList<Token> tokens;

    public GeneratedToken(CorrelationID correlationID, ArrayList<Token> tokens) {
        super(correlationID);
        this.tokens = tokens;
    }
}