package messageUtilities.cqrs.queries;

import messageUtilities.CorrelationID;
import messageUtilities.Message;

public abstract class Query extends Message {

    public Query(CorrelationID correlationID) {
        super(correlationID);
    }
}
