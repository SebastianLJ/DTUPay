package messageUtilities.stubs;

import messageUtilities.MessageEvent;
import messageUtilities.queues.IDTUPayMessageQueue;

/**
 * @Autor Jákup Viljam Dam - s185095
 */
public class ConsumerStub {
    private final IDTUPayMessageQueue messageQueue;

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public ConsumerStub(IDTUPayMessageQueue messageQueue) {
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
        MessageEvent newEvent = new MessageEvent("EventCreatedStub", new Object[]{createdEvent});
        messageQueue.publish(newEvent);
    }
}
