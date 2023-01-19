package messageUtilities.queues;

import messageUtilities.cqrs.events.Event2;

import java.util.function.Consumer;

/**
 * @Autor Jákup Viljam Dam - s185095
 * Used the messageUtilities from the course
 */
public interface IDTUPayMessageQueue2 {
    void publish(Event2 message);
    void addHandler(String eventType, Consumer<Event2> handler);
}
