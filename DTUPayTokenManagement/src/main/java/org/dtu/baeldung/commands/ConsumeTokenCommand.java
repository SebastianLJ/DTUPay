package org.dtu.baeldung.commands;

import lombok.Data;
import org.dtu.baeldung.model.Token;

@Data
public class ConsumeTokenCommand {
    private Token token;
}
