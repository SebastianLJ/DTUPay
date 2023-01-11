package messageQueue;

import java.util.function.Consumer;

public interface IDTUPayMessageQueue {
    void publish(Event message);
    void addHandler(EventType eventType, Consumer<Event> handler);
}
