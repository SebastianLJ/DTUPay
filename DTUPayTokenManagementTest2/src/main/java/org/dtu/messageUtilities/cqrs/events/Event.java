package org.dtu.messageUtilities.cqrs.events;

import org.dtu.messageUtilities.CorrelationID;
import org.dtu.messageUtilities.Message;

import java.io.Serializable;

public abstract class Event extends Message implements Serializable {


    private static final long serialVersionUID = 5788074693676430531L;

    public Event() {
        super();
    }

    public Event(CorrelationID correlationID) {
        super(correlationID);
    }
}
