package messageUtilities.eventSource;

import messageUtilities.events.Event;

import java.util.stream.Stream;

public interface IEventStore<T> {
    void addEvent(T obj, Event event);
    void addEvents(T obj, Stream<Event> events);
    Stream<Event> getEventsFor(T obj, Stream<Event> events);
}
