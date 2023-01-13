package messageUtilities.events;

import lombok.Getter;
import messageUtilities.queues.IDTUPayMessage;

import java.io.Serializable;
import java.util.UUID;

public abstract class Event implements IDTUPayMessage, Serializable {

    public String message = "";

    public Event() {}
}
