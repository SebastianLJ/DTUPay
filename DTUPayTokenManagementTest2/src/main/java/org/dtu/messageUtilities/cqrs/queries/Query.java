package org.dtu.messageUtilities.cqrs.queries;

import org.dtu.messageUtilities.CorrelationID;
import org.dtu.messageUtilities.Message;

public abstract class Query extends Message {

    public Query() {
        super();
    }

    public Query(CorrelationID correlationID) {
        super(correlationID);
    }
}
