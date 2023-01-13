package messageUtilities;

import messageUtilities.events.Event;
import messageUtilities.events.EventID;

public class EventCreatedStub extends Event {
    public EventCreatedStub(EventID eventID) {
        super(eventID);
    }
}
