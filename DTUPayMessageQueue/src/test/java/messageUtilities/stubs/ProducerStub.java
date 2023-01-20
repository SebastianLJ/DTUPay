package messageUtilities.stubs;

import messageUtilities.cqrs.CorrelationID;
import messageUtilities.MessageEvent;
import messageUtilities.queues.IDTUPayMessageQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @Autor Jákup Viljam Dam - s185095
 */
public class ProducerStub {

    private final Map<CorrelationID, CompletableFuture<EventCreatedStub>> correlations = new HashMap<>();
    private final IDTUPayMessageQueue messageQueue;

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public ProducerStub(IDTUPayMessageQueue messageQueue) {
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
        correlations.put(event.getCorrelationID(), new CompletableFuture<>());
        System.out.println( "RequestedEvent CorrelationID: " + event.getCorrelationID());
        MessageEvent newEvent = new MessageEvent("EventRequestedStub", new Object[]{event});
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
