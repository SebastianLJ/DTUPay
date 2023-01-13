package org.dtu.factories;

import messageUtilities.queues.QueueType;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import org.dtu.services.CustomerService;

public class CustomerFactory {
    static CustomerService service = null;



    public synchronized CustomerService getService() {
        if (service == null) {
            service = new CustomerService(new DTUPayRabbitMQ(QueueType.DTUPay_TokenManagement));
        }
        return service;
    }


}
