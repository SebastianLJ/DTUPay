package messageUtilities.stubs;

import messageUtilities.queues.IDTUPayMessageQueue;

import java.util.concurrent.CompletableFuture;

public class ProducerStub {

    public EventCreatedStub currentEventCreated;
    public EventRequestedStub currentEventRequested;
    private CompletableFuture<EventCreatedStub> eventCreatedFuture;
    private final IDTUPayMessageQueue messageQueue;

    public ProducerStub(IDTUPayMessageQueue messageQueue) {
        this.messageQueue = messageQueue;
        this.messageQueue.addHandler(EventCreatedStub.class, e -> produceQueueEvent((EventCreatedStub) e));
    }

    public EventCreatedStub produceEvent(EventRequestedStub event) {
        this.currentEventRequested = event;
        eventCreatedFuture = new CompletableFuture<>();
        messageQueue.publish(event);
        return eventCreatedFuture.join();
    }

    private void produceQueueEvent(EventCreatedStub event) {
        this.currentEventCreated = event;
        eventCreatedFuture.complete(this.currentEventCreated);
    }
}
