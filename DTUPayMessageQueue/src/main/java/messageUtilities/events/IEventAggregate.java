package messageUtilities.events;

import java.util.stream.Stream;

public interface IEventAggregate<T> {
    T create(Object[] args);
    T createFromEvents(Stream<Event> events);
    void registerEventHandlers();
    void applyEvents(Stream<Event> events) throws Error;
    void applyEvent(Event event);
    void clearAppliedEvents();
}
