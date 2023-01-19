package messageUtilities.cqrs.commands;

import messageUtilities.CorrelationID;
import messageUtilities.Message;

/**
 * @Autor Jákup Viljam Dam - s185095
 */
public abstract class Command extends Message {

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public Command() {
        super();
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public Command(CorrelationID correlationID) {
        super(correlationID);
    }
}
