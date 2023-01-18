package org.dtu.messageUtilities;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

@Value
public class CorrelationID implements Serializable {
    UUID id;

    public static CorrelationID randomID() {
        return new CorrelationID(UUID.randomUUID());
    }
}
