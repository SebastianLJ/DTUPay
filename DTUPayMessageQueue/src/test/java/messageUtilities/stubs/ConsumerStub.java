package messageUtilities.stubs;

import messageUtilities.queues.IDTUPayMessageQueue;

public class ConsumerStub {
    private final IDTUPayMessageQueue messageQueue;

    public ConsumerStub(IDTUPayMessageQueue messageQueue) {
        this.messageQueue = messageQueue;
        this.messageQueue.addHandler(EventRequestedStub.class, e -> consumeQueueEvent((EventRequestedStub) e));
    }

    private void consumeQueueEvent(EventRequestedStub event) {
        EventCreatedStub newEvent = new EventCreatedStub(event.getCorrelationID());
        newEvent.setMessage("World!");
        System.out.println(newEvent.getCorrelationID().toString() + " " + newEvent.getMessage());
        messageQueue.publish(newEvent);
    }
}
