package messageUtilities;

import messageUtilities.queues.IDTUPayMessage;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public abstract class Message implements IDTUPayMessage, Serializable {

    public final UUID id = UUID.randomUUID();
    public final Date date = new Date();
    public String message = "";

}
