package messageUtilities.queues;

import messageUtilities.MessageEvent;

import java.util.function.Consumer;

/**
 * @Autor Jákup Viljam Dam - s185095
 * Used the messageUtilities from the course
 */
public interface IDTUPayMessageQueue2 {
    void publish(MessageEvent message);
    void addHandler(String eventType, Consumer<MessageEvent> handler);
}
