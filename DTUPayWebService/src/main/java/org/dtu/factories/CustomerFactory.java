package org.dtu.factories;

import messageUtilities.queues.IDTUPayMessageQueue;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMq;
import org.dtu.repositories.CustomerRepository;
import org.dtu.services.CustomerService;

public class CustomerFactory {
    static CustomerService service = null;

    public synchronized CustomerService getService() {
        if (service == null) {
            service = new CustomerService(
                    new DTUPayRabbitMq(
                            "rabbitmq"
                    ),
                    new CustomerRepository()
            );
        }
        return service;
    }

    public synchronized CustomerService getService(IDTUPayMessageQueue messageQueue2) {
        if (service == null) {
            service = new CustomerService(
                    messageQueue2,
                    new CustomerRepository()
            );
        }
        return service;
    }


}
