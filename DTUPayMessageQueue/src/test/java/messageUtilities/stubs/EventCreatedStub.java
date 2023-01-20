package messageUtilities.stubs;

import messageUtilities.cqrs.CorrelationID;
import messageUtilities.cqrs.events.Event;

/**
 * @Autor Jákup Viljam Dam - s185095
 */
public class EventCreatedStub extends Event {

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public EventCreatedStub(CorrelationID correlationID) {
        super(correlationID);
    }
}
