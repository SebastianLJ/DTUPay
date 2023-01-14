package messageUtilities.cqrs.events;

import messageUtilities.CorrelationID;
import messageUtilities.Message;

public abstract class Event extends Message {

    public Event(CorrelationID correlationID) {
        super(correlationID);
    }
}
