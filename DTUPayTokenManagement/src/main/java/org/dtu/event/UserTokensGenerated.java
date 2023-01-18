package org.dtu.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import messageUtilities.cqrs.events.Event;
import org.dtu.domain.Token;
import org.dtu.aggregate.UserId;

import java.util.List;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserTokensGenerated extends Event {
    private static final long serialVersionUID = -9043605699436268036L;
    UserId userId;
    List<Token> tokens;
}
