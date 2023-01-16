package org.dtu.baeldung.repositories;

import org.dtu.baeldung.events.Event;

import java.util.*;

public class EventStore {
    private Map<UUID, List<Event>> store = new HashMap<>();

    public void addEvent(UUID userId, Event event){
        List<Event> events = store.get(userId);
        if (events == null) {
            events = new ArrayList<Event>();
            events.add(event);
            store.put(userId, events);
        } else {
            events.add(event);
        }
    }

    public List<Event> getEvents(UUID userId) {
        return store.get(userId);
    }
}
