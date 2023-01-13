package messageUtilities.events;

import java.util.stream.Stream;

public interface IEventStore<T> {
    void addEvent(T obj, Event event);
    void addEvents(T obj, Stream<Event> events);
    T getEventsFor(Stream<Event> events);
}
