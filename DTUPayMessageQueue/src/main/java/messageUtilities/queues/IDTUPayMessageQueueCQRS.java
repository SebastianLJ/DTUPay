package messageUtilities.queues;

import java.util.function.Consumer;

/**
 * @Autor Jákup Viljam Dam - s185095
 * Used the messageUtilities from the course
 */
public interface IDTUPayMessageQueueCQRS {
    void publish(IDTUPayMessage event);
    void addHandler(Class<? extends IDTUPayMessage> event, Consumer<IDTUPayMessage> handler);
}
