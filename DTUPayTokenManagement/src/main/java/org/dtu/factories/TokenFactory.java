package org.dtu.factories;

import messageUtilities.queues.QueueType;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ2;
import messageUtilities.queues.rabbitmq.HostnameType;
import org.dtu.services.TokenService;

/**
 * @author Alexander Faarup Christensen - s174355
 */
public class TokenFactory {

    static TokenService service = null;

    public synchronized TokenService getService() {
        if (service == null) {
            service = new TokenService(new DTUPayRabbitMQ2("rabbitmq"));
        }
        return service;
    }
}
