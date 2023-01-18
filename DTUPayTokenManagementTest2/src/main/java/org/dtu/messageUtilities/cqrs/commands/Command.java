package org.dtu.messageUtilities.cqrs.commands;

import org.dtu.messageUtilities.CorrelationID;
import org.dtu.messageUtilities.Message;

public abstract class Command extends Message {

    public Command() {
        super();
    }

    public Command(CorrelationID correlationID) {
        super(correlationID);
    }
}
