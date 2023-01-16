package messageUtilities.stubs;

import com.rabbitmq.client.impl.ForgivingExceptionHandler;
import messageUtilities.CorrelationID;
import messageUtilities.queues.IDTUPayMessageQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ProducerStub {

    private final Map<CorrelationID, CompletableFuture<EventCreatedStub>> correlations = new HashMap<>();
    private final IDTUPayMessageQueue messageQueue;

    public ProducerStub(IDTUPayMessageQueue messageQueue) {
        this.messageQueue = messageQueue;
        this.messageQueue.addHandler(EventCreatedStub.class, e -> produceQueueEvent((EventCreatedStub) e));
    }

    public EventCreatedStub produceEvent(EventRequestedStub event) {
        System.out.println(event.getCorrelationID().toString() + " " + event.getMessage());
        correlations.put(event.getCorrelationID(), new CompletableFuture<>());
        messageQueue.publish(event);
        return correlations.get(event.getCorrelationID()).join();
    }

    private void produceQueueEvent(EventCreatedStub event) {
        try {
            correlations.get(event.getCorrelationID()).complete(event);
        } catch (Exception e) {

        }
    }
}
