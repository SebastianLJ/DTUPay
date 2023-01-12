package messageUtilities.events;

import com.google.gson.Gson;
import messageUtilities.queues.IDTUPayMessage;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Event implements IDTUPayMessage, Serializable {

    public Event() { }
}
