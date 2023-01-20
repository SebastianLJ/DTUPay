package messageUtilities.eventSource;

import messageUtilities.cqrs.events.Event;

import java.util.stream.Stream;

/**
 * @Autor Jákup Viljam Dam - s185095
 */
public interface IEventStore<T> {
    void addEvent(T obj, Event event);
    void addEvents(T obj, Stream<Event> events);
    Stream<Event> getEventsFor(T obj, Stream<Event> events);
}
