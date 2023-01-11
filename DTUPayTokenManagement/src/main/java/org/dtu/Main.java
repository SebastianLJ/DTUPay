package org.dtu;

import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import messageUtilities.queues.rabbitmq.QueueType;

public class Main {
    public static void main(String[] args) {
        DTUPayRabbitMQ messageQueue = new DTUPayRabbitMQ(QueueType.DTUPay_TokenManagement);


    }
}