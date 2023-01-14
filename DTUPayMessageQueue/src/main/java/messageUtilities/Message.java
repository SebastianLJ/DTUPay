package messageUtilities;

import lombok.Getter;
import lombok.Setter;
import messageUtilities.queues.IDTUPayMessage;

import java.io.Serializable;
import java.util.Date;

public abstract class Message implements IDTUPayMessage, Serializable {

    @Getter @Setter
    private String message;
    @Getter
    private CorrelationID correlationID;
    @Getter
    private final Date date;

    public Message() {
        this.correlationID = null;
        this.message = "";
        this.date = new Date();
    }

    public Message(CorrelationID correlationID) {
        this.correlationID = correlationID;
        this.message = "";
        this.date = new Date();
    }

    public Message(CorrelationID correlationID, String message, Date date) {
        this.correlationID = correlationID;
        this.message = message;
        this.date = date;
    }
}
