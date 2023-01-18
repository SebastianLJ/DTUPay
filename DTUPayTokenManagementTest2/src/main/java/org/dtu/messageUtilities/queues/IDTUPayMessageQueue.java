package org.dtu.messageUtilities.queues;

import java.util.function.Consumer;

public interface IDTUPayMessageQueue {
    void publish(IDTUPayMessage event);
    void addHandler(Class<? extends IDTUPayMessage> event, Consumer<IDTUPayMessage> handler);
}
