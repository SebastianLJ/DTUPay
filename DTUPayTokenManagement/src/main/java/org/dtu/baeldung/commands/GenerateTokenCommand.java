package org.dtu.baeldung.commands;

import lombok.Data;
import org.dtu.baeldung.model.Token;

import java.util.UUID;

@Data
public class GenerateTokenCommand {
    private UUID userId;
    private int amount;
}
