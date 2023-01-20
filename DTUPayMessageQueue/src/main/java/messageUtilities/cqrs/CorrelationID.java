package messageUtilities.cqrs;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * @Autor Jákup Viljam Dam - s185095
 */
@Value
public class CorrelationID implements Serializable {
    UUID id;

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public static CorrelationID randomID() {
        return new CorrelationID(UUID.randomUUID());
    }
}
