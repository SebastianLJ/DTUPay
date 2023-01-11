package messageUtilities.queues.cqrs;

import messageUtilities.queues.IDTUPayMessage;
import messageUtilities.queues.IDTUPayMessageQueue;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class DTUPayCQRSSync extends DTUPayCQRS implements IDTUPayMessageQueue {

    private final BlockingQueue<IDTUPayMessage> queue = new LinkedBlockingQueue<IDTUPayMessage>();

    public DTUPayCQRSSync() {
        Thread notificationThread = new Thread(this::notifySubscribers);
        notificationThread.start();
    }

    @Override
    public void publish(IDTUPayMessage event) {
        try {
            queue.put(event);
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    @Override
    public void addHandler(Class<? extends IDTUPayMessage> event, Consumer<IDTUPayMessage> handler) {
        if (!subscribers.containsKey(event)) {
            subscribers.put(event, new ArrayList<>());
        }
        subscribers.get(event).add(handler);
    }

    private void notifySubscribers() {
        while (true) {
            IDTUPayMessage message;
            try {
                message = queue.take();
            } catch (InterruptedException e) {
                throw new Error(e);
            }
            subscribers.getOrDefault(message.getClass(), new ArrayList<>())
                    .forEach(a -> a.accept(message));
        }
    }
}
