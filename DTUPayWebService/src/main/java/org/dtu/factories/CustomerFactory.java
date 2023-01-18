package org.dtu.factories;

import messageUtilities.queues.IDTUPayMessageQueue2;
import messageUtilities.queues.QueueType;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ2;
import messageUtilities.queues.rabbitmq.HostnameType;
import org.dtu.repositories.CustomerRepository;
import org.dtu.services.CustomerService;

public class CustomerFactory {
    static CustomerService service = null;

    public synchronized CustomerService getService() {
        if (service == null) {
            service = new CustomerService(
                    new DTUPayRabbitMQ2(
                            "rabbitmq"
                    ),
                    new CustomerRepository()
            );
        }
        return service;
    }

    public synchronized CustomerService getService(IDTUPayMessageQueue2 messageQueue2) {
        if (service == null) {
            service = new CustomerService(
                    messageQueue2,
                    new CustomerRepository()
            );
        }
        return service;
    }


}
