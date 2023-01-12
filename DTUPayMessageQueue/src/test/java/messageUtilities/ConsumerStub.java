package messageUtilities;

import messageUtilities.queues.IDTUPayMessageQueue;

public class ConsumerStub {

    public EventRequestedStub currentEventRequested;
    public EventCreatedStub currentEventCreated;
    private IDTUPayMessageQueue messageQueue;

    public ConsumerStub(IDTUPayMessageQueue messageQueue) {
        this.messageQueue = messageQueue;
        this.messageQueue.addHandler(EventRequestedStub.class, e -> consumeQueueEvent((EventRequestedStub) e));
    }

    private void consumeQueueEvent(EventRequestedStub event) {
        this.currentEventRequested = event;
        this.currentEventCreated = new EventCreatedStub();
        this.currentEventCreated.message = "createdEvent";
        messageQueue.publish(this.currentEventCreated);
    }
}