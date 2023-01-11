package messageUtilities.events;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Event implements Serializable {

    private EventType eventType;
    private Object[] arguments;

    public Event() { }

    public Event(EventType eventType, Object[] arguments) {
        this.eventType = eventType;
        this.arguments = arguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return eventType == event.eventType && Arrays.equals(arguments, event.arguments);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(eventType);
        result = 31 * result + Arrays.hashCode(arguments);
        return result;
    }

    @Override
    public String toString() {
        return "etst.Event{" +
                "eventType=" + eventType +
                ", arguments=" + Arrays.toString(arguments) +
                '}';
    }

    public EventType getEventType() {
        return eventType;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public <T> T getArgument(int i, Class<T> cls) {
        var gson = new Gson();
        var jsonString = gson.toJson(arguments[i]);
        return gson.fromJson(jsonString, cls);
    }

}
