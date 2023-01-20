package org.dtu.factories;


import messageUtilities.queues.rabbitmq.DTUPayRabbitMq;
import org.dtu.repositories.PaymentRepository;
import org.dtu.services.ReportService;
/**
 * @author Sebastian Juste pedersen (s205335)
 * @author Nicklas Olabi (s205347)
 */

public class ReportFactory {
    static ReportService service = null;

    public synchronized ReportService getService() {
        if (service == null) {
            service = new ReportService(new DTUPayRabbitMq("rabbitmq"), PaymentRepository.getInstance() );
        }
        return service;
    }
}
