package messageUtilities.events;

import messageUtilities.queues.IDTUPayMessage;

import java.io.Serializable;

public abstract class Event implements IDTUPayMessage, Serializable {

    public String message = "";

    public Event() {

    }
}
