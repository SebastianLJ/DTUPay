package messageUtilities.stubs;

import messageUtilities.cqrs.events.Event2;
import messageUtilities.queues.IDTUPayMessageQueue;
import messageUtilities.queues.IDTUPayMessageQueue2;

/**
 * @Autor Jákup Viljam Dam - s185095
 */
public class ConsumerStub {
    private final IDTUPayMessageQueue2 messageQueue;

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public ConsumerStub(IDTUPayMessageQueue2 messageQueue) {
        this.messageQueue = messageQueue;
        this.messageQueue.addHandler("EventRequestedStub", e -> {
            EventRequestedStub newEvent = e.getArgument(0, EventRequestedStub.class);
            consumeQueueEvent(newEvent);
        });
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public void consumeQueueEvent(EventRequestedStub event) {
        EventCreatedStub createdEvent = new EventCreatedStub(event.getCorrelationID());
        System.out.println( "CreatedEvent CorrelationID: " + createdEvent.getCorrelationID());
        Event2 newEvent = new Event2("EventCreatedStub", new Object[]{createdEvent});
        messageQueue.publish(newEvent);
    }
}
