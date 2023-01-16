package org.dtu.baeldung.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dtu.baeldung.model.Token;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ConsumeTokenEvent extends Event {
    private Token token;
}
