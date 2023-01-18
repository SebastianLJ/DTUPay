package messageUtilities.queues;

import messageUtilities.cqrs.events.Event2;

import java.util.function.Consumer;

public interface IDTUPayMessageQueue2 {
    void publish(Event2 message);
    void addHandler(String eventType, Consumer<Event2> handler);
}
