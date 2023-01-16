package messageUtilities.stubs;

import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;

public class EventRequestedStub extends Event {

    public EventRequestedStub(CorrelationID correlationID) {
        super(correlationID);
    }
}
