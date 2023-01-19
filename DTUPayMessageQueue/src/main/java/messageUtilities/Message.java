package messageUtilities;

import lombok.Getter;
import lombok.Setter;
import messageUtilities.cqrs.CorrelationID;
import messageUtilities.queues.IDTUPayMessage;

import java.io.Serializable;
import java.util.Date;

/**
 * @Autor Jákup Viljam Dam - s185095
 */
public abstract class Message implements IDTUPayMessage, Serializable {

    @Getter @Setter
    private String message;
    @Getter
    private final CorrelationID correlationID;
    @Getter
    private final Date date;

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public Message() {
        this.correlationID = null;
        this.message = "";
        this.date = new Date();
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public Message(CorrelationID correlationID) {
        this.correlationID = correlationID;
        this.message = "";
        this.date = new Date();
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public Message(CorrelationID correlationID, String message, Date date) {
        this.correlationID = correlationID;
        this.message = message;
        this.date = date;
    }
}
