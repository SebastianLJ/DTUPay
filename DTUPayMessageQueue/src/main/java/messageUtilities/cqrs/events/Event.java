package messageUtilities.cqrs.events;

import messageUtilities.cqrs.CorrelationID;
import messageUtilities.Message;

import java.io.Serializable;

/**
 * @Autor Jákup Viljam Dam - s185095
 */
public abstract class Event extends Message implements Serializable {

    private static final long serialVersionUID = 5788074693676430531L;

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public Event() {
        super();
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public Event(CorrelationID correlationID) {
        super(correlationID);
    }
}
