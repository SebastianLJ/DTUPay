package org.dtu.factories;

import messageUtilities.queues.rabbitmq.DTUPayRabbitMq;
import org.dtu.repositories.MerchantRepository;
import org.dtu.repositories.PaymentRepository;
import org.dtu.services.MerchantService;
/**
 * @author Sebastian Juste pedersen (s205335)
 * @author Nicklas Olabi (s205347)
 */

public class MerchantFactory {
    static MerchantService service = null;

    public synchronized MerchantService getService() {
        if (service == null) {
            service = new MerchantService(
                    new DTUPayRabbitMq(
                            "rabbitmq"
                    ),
                    new MerchantRepository(),
                    PaymentRepository.getInstance()
            );
        }
        return service;
    }
}
