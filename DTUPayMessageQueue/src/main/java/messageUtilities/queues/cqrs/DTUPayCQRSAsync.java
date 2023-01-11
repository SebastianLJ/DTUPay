package messageUtilities.queues.cqrs;

import messageUtilities.queues.IDTUPayMessage;
import messageUtilities.queues.IDTUPayMessageQueue;

import java.util.ArrayList;
import java.util.function.Consumer;

public class DTUPayCQRSAsync extends DTUPayCQRS implements IDTUPayMessageQueue {

    @Override
    public void publish(IDTUPayMessage event) {
        notifySubscribers(event);
    }

    @Override
    public void addHandler(Class<? extends IDTUPayMessage> event, Consumer<IDTUPayMessage> handler) {
        if (!subscribers.containsKey(event)) {
            subscribers.put(event, new ArrayList<>());
        }
        subscribers.get(event).add(handler);
    }

    private void notifySubscribers(IDTUPayMessage event) {
        subscribers.getOrDefault(event.getClass(), new ArrayList<>()).forEach(a -> a.accept(event));
    }
}
