package messageUtilities.cqrs.queries;

import messageUtilities.CorrelationID;
import messageUtilities.Message;

/**
 * @Autor Jákup Viljam Dam - s185095
 */
public abstract class Query extends Message {

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public Query() {
        super();
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public Query(CorrelationID correlationID) {
        super(correlationID);
    }
}
