package org.dtu.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;
/*import org.dtu.aggregate.Token;*/
import org.dtu.domain.Token;
import org.dtu.aggregate.UserId;

import java.util.ArrayList;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TokensGenerated extends Event {

    UserId userid;
    ArrayList<Token> tokens;

    public TokensGenerated(CorrelationID correlationID, UserId userid, ArrayList<Token> tokens) {
        super(correlationID);
        this.userid = userid;
        this.tokens = tokens;
    }
}