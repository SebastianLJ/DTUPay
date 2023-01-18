package org.dtu.services;

import messageUtilities.CorrelationID;
import messageUtilities.queues.IDTUPayMessageQueue;
import org.dtu.aggregate.Payment;
import org.dtu.aggregate.UserId;
import org.dtu.events.CustomerReportGenerated;
import org.dtu.events.FullReportGenerated;
import org.dtu.events.MerchantReportGenerated;
import org.dtu.exceptions.PaymentNotFoundException;
import org.dtu.repositories.PaymentRepository;

import java.util.List;
import java.util.UUID;

public class ReportService {

    PaymentRepository repository;
    IDTUPayMessageQueue messageQueue;

    public ReportService(IDTUPayMessageQueue messageQueue, PaymentRepository repository) {
        this.messageQueue = messageQueue;
        this.repository = repository;
    }

    public List<Payment> getPayments() throws PaymentNotFoundException {
        List<Payment> payments = repository.getPayments();
        CorrelationID correlationID = CorrelationID.randomID();
        FullReportGenerated event = new FullReportGenerated(payments);
        System.out.println("Created FullReportGenerated event: " + correlationID);
        messageQueue.publish(event);
        System.out.println("Published FullReportGenerated event: " + correlationID);
        return payments;
    }

    public List<Payment> getPaymentByMerchantId(UserId id) throws PaymentNotFoundException {
        List<Payment> merchantPayments = repository.getPaymentsByMerchantId(id.getUuid());
        CorrelationID correlationID = CorrelationID.randomID();
        MerchantReportGenerated event = new MerchantReportGenerated(id, merchantPayments);
        System.out.println("Created MerchantReportGenerated event: " + correlationID);
        messageQueue.publish(event);
        System.out.println("Published MerchantReportGenerated event: " + correlationID);
        return merchantPayments;
    }

//    public List<Payment> getPaymentByCustomerId(UserId id) throws PaymentNotFoundException {
//        List<Payment> customerPayments = repository.getPaymentsByToken();
//        CustomerReportGenerated event = new CustomerReportGenerated(id, customerPayments);
//        messageQueue.publish(event);
//        return customerPayments;
//
//    }
}
