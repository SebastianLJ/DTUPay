package org.dtu.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.dtu.aggregate.Token;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ConsumedToken extends TokenEvent {
    Token token;
}

