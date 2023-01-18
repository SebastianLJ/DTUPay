package org.dtu.messageUtilities.eventSource;

import org.dtu.messageUtilities.cqrs.events.Event;

import java.util.stream.Stream;

public interface IEventStore<T> {
    void addEvent(T obj, Event event);
    void addEvents(T obj, Stream<Event> events);
    Stream<Event> getEventsFor(T obj, Stream<Event> events);
}
