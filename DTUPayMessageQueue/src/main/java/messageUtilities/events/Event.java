package messageUtilities.events;

import lombok.Getter;
import messageUtilities.queues.IDTUPayMessage;

import java.io.Serializable;

public abstract class Event implements IDTUPayMessage, Serializable {

    public String message = "";
    @Getter
    private final EventID eventID;

    public Event(EventID eventID) {
        this.eventID = eventID;
    }
}
