package messageUtilities.stubs;

import messageUtilities.cqrs.CorrelationID;
import messageUtilities.cqrs.events.Event;

/**
 * @Autor Jákup Viljam Dam - s185095
 */
public class EventRequestedStub extends Event {

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public EventRequestedStub(CorrelationID correlationID) {
        super(correlationID);
    }
}
