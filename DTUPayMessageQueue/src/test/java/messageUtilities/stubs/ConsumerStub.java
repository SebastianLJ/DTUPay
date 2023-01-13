package messageUtilities.stubs;

import messageUtilities.queues.IDTUPayMessageQueue;

public class ConsumerStub {

    public EventRequestedStub currentEventRequested;
    public EventCreatedStub currentEventCreated;
    private final IDTUPayMessageQueue messageQueue;

    public ConsumerStub(IDTUPayMessageQueue messageQueue) {
        this.messageQueue = messageQueue;
        this.messageQueue.addHandler(EventRequestedStub.class, e -> consumeQueueEvent((EventRequestedStub) e));
    }

    private void consumeQueueEvent(EventRequestedStub event) {
        this.currentEventRequested = event;
        this.currentEventCreated = new EventCreatedStub();
        this.currentEventCreated.message = "World!";
        messageQueue.publish(this.currentEventCreated);
    }
}
