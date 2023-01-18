package org.dtu.factories;

import org.dtu.messageUtilities.queues.QueueType;
import org.dtu.messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import org.dtu.messageUtilities.queues.rabbitmq.HostnameType;
import org.dtu.services.TokenService;

public class TokenFactory {

    static TokenService service = null;

    public synchronized TokenService getService() {
        if (service == null) {
            service = new TokenService(new DTUPayRabbitMQ(QueueType.DTUPay, HostnameType.rabbitMq));
        }
        return service;
    }
}
