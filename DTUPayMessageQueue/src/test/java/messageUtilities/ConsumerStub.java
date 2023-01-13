package messageUtilities;

import messageUtilities.events.EventID;
import messageUtilities.queues.IDTUPayMessageQueue;

import java.util.UUID;

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
        this.currentEventCreated = new EventCreatedStub(new EventID(UUID.randomUUID()));
        this.currentEventCreated.message = "createdEvent";
        messageQueue.publish(this.currentEventCreated);
    }
}
