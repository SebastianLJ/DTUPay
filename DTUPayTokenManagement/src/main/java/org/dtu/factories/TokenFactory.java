package org.dtu.factories;

import messageUtilities.queues.QueueType;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import org.dtu.services.TokenService;

public class TokenFactory {

    static TokenService service = null;

    public synchronized TokenService getService() {
        if (service == null) {
            service = new TokenService(new DTUPayRabbitMQ(QueueType.DTUPay));
        }
        return service;
    }
}
