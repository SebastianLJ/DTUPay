package org.dtu.baeldung.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GenerateTokenEvent extends Event {
    private UUID userId;
    private int amount;
}
