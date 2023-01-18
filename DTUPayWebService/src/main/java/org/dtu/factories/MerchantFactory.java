package org.dtu.factories;

import messageUtilities.queues.QueueType;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import messageUtilities.queues.rabbitmq.HostnameType;
import org.dtu.repositories.MerchantRepository;
import org.dtu.repositories.PaymentRepository;
import org.dtu.services.MerchantService;

public class MerchantFactory {
    static MerchantService service = null;

    public synchronized MerchantService getService() {
        if (service == null) {
            service = new MerchantService(
                    new DTUPayRabbitMQ(
                            QueueType.DTUPay,
                            HostnameType.rabbitMq
                    ),
                    new MerchantRepository(),
                    new PaymentRepository()
            );
        }
        return service;
    }
}
