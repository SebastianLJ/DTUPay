package messageUtilities.stubs;

import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event2;
import messageUtilities.queues.IDTUPayMessageQueue;
import messageUtilities.queues.IDTUPayMessageQueue2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @Autor Jákup Viljam Dam - s185095
 */
public class ProducerStub {

    private final Map<CorrelationID, CompletableFuture<EventCreatedStub>> correlations = new HashMap<>();
    private final IDTUPayMessageQueue2 messageQueue;

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public ProducerStub(IDTUPayMessageQueue2 messageQueue) {
        this.messageQueue = messageQueue;
        this.messageQueue.addHandler("EventCreatedStub", e -> {
            EventCreatedStub newEvent = e.getArgument(0, EventCreatedStub.class);
            produceQueueEvent(newEvent);
        });
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public EventCreatedStub produceEvent(EventRequestedStub event) {
        System.out.println(event.getCorrelationID().toString() + " " + event.getMessage());
        correlations.put(event.getCorrelationID(), new CompletableFuture<>());
        Event2 newEvent = new Event2("EventRequestedStub", new Object[]{event});
        messageQueue.publish(newEvent);
        return correlations.get(event.getCorrelationID()).join();
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public void produceQueueEvent(EventCreatedStub event) {
        correlations.get(event.getCorrelationID()).complete(event);
    }
}
