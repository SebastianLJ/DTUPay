package messageUtilities.stubs;

import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;

public class EventCreatedStub extends Event {

    public EventCreatedStub(CorrelationID correlationID) {
        super(correlationID);
    }
}
