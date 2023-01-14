package messageUtilities.cqrs.commands;

import messageUtilities.CorrelationID;
import messageUtilities.Message;

public abstract class Command extends Message {

    public Command() {
        super();
    }

    public Command(CorrelationID correlationID) {
        super(correlationID);
    }
}
