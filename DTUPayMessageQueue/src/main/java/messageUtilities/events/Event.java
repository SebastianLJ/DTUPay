package messageUtilities.events;

import messageUtilities.queues.IDTUPayMessage;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public abstract class Event implements IDTUPayMessage, Serializable {

    public final UUID eventID = UUID.randomUUID();
    public final Date date = new Date();
    public String message = "";

    public Event() {

    }
}
