package messageUtilities;

import messageUtilities.events.Event;
import messageUtilities.events.EventID;

public class EventRequestedStub extends Event {

    public EventRequestedStub(EventID eventID) {
        super(eventID);
    }
}
