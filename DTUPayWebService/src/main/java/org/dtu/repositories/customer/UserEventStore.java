package org.dtu.repositories.customer;

import messageUtilities.events.Event;
import messageUtilities.eventSource.IEventStore;
import messageUtilities.queues.IDTUPayMessageQueue;
import org.dtu.aggregate.UserId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class UserEventStore implements IEventStore<UserId> {

    private final Map<UserId, List<Event>> store = new ConcurrentHashMap<>();
    private final IDTUPayMessageQueue eventBus;

    public UserEventStore(IDTUPayMessageQueue eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void addEvent(UserId obj, Event event) {
        if (!store.containsKey(obj)) {
            store.put(obj, new ArrayList<>());
        }
        store.get(obj).add(event);
        eventBus.publish(event);
    }

    @Override
    public void addEvents(UserId obj, Stream<Event> events) {
        events.forEach(e -> addEvent(obj, e));
    }

    @Override
    public Stream<Event> getEventsFor(UserId obj, Stream<Event> events) {
        if (!store.containsKey(obj)) {
            store.put(obj, new ArrayList<>());
        }
        return store.get(obj).stream();
    }
}
