package org.dtu.factories;

import messageUtilities.queues.rabbitmq.DTUPayRabbitMq;
import org.dtu.repositories.MerchantRepository;
import org.dtu.repositories.PaymentRepository;
import org.dtu.services.MerchantService;

public class MerchantFactory {
    static MerchantService service = null;

    public synchronized MerchantService getService() {
        if (service == null) {
            service = new MerchantService(
                    new DTUPayRabbitMq(
                            "rabbitmq"
                    ),
                    new MerchantRepository(),
                    new PaymentRepository()
            );
        }
        return service;
    }
}
