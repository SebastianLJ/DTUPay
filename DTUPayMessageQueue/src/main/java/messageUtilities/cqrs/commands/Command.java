package messageUtilities.cqrs.commands;

import messageUtilities.CorrelationID;
import messageUtilities.Message;

public abstract class Command extends Message {

    public Command(CorrelationID correlationID) {
        super(correlationID);
    }
}
