package messageUtilities.queues.cqrs;

import messageUtilities.queues.IDTUPayMessage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public abstract class DTUPayCQRS {

    protected Map<Class<?>, List<Consumer<IDTUPayMessage>>> subscribers = new ConcurrentHashMap<>();

}
