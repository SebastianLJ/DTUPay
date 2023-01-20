package org.dtu.factories;


import messageUtilities.queues.rabbitmq.DTUPayRabbitMq;
import org.dtu.repositories.PaymentRepository;
import org.dtu.services.ReportService;

public class ReportFactory {
    static ReportService service = null;

    public synchronized ReportService getService() {
        if (service == null) {
            service = new ReportService(new DTUPayRabbitMq("rabbitmq"), new PaymentRepository() );
        }
        return service;
    }
}
