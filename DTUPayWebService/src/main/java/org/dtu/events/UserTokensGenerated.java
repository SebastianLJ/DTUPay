package org.dtu.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.cqrs.CorrelationID;
import messageUtilities.cqrs.events.Event;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;

import java.util.List;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserTokensGenerated extends Event {
    private static final long serialVersionUID = -9043605699436268036L;
    UserId userId;
    List<Token> tokens;

    public UserTokensGenerated(CorrelationID correlationID, UserId userId, List<Token> tokens){
        super(correlationID);
        this.userId = userId;
        this.tokens=tokens;
    }
}
