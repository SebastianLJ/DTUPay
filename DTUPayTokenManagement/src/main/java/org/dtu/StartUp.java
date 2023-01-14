package org.dtu;

import messageUtilities.queues.QueueType;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import org.dtu.factories.TokenFactory;

public class StartUp {
    public static void main(String[] args) {
        new StartUp().startTokenManagement();
    }

    private void startTokenManagement() {
        new TokenFactory(new DTUPayRabbitMQ(QueueType.DTUPay));
    }
}