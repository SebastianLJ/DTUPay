package org.dtu.factories;

import messageUtilities.queues.rabbitmq.DTUPayRabbitMq;
import org.dtu.services.TokenService;

/**
 * @author Alexander Faarup Christensen - s174355
 */
public class TokenFactory {

    static TokenService service = null;

    public synchronized TokenService getService() {
        if (service == null) {
            service = new TokenService(new DTUPayRabbitMq("rabbitmq"));
        }
        return service;
    }
}
