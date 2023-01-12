package org.dtu.factories;

import messageUtilities.queues.IDTUPayMessageQueue;
import messageUtilities.queues.QueueType;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import org.dtu.services.TokenService;

public class TokenFactory {

    static TokenService service = null;
    static IDTUPayMessageQueue messageQueue = new DTUPayRabbitMQ(QueueType.DTUPay_TokenManagement);

    public synchronized TokenService getService() {
        if (service == null) {
            service = new TokenService(messageQueue);
        }
        return service;
    }
}
