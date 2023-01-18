package org.dtu.factories;


import messageUtilities.queues.QueueType;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import messageUtilities.queues.rabbitmq.HostnameType;
import org.dtu.repositories.PaymentRepository;
import org.dtu.services.ReportService;

public class ReportFactory {
    static ReportService service = null;

    public synchronized ReportService getService() {
        if (service == null) {
            service = new ReportService(new DTUPayRabbitMQ(QueueType.DTUPay, HostnameType.rabbitMq), new PaymentRepository() );
        }
        return service;
    }
}