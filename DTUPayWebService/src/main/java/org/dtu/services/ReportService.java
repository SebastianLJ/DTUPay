package org.dtu.services;

import com.sun.xml.bind.v2.TODO;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event2;
import messageUtilities.queues.IDTUPayMessageQueue;
import messageUtilities.queues.IDTUPayMessageQueue2;
import org.dtu.aggregate.Payment;
import org.dtu.aggregate.UserId;
import org.dtu.domain.Token;
import org.dtu.events.CustomerReportGenerated;
import org.dtu.events.FullReportGenerated;
import org.dtu.events.MerchantReportGenerated;
import org.dtu.exceptions.PaymentNotFoundException;
import org.dtu.repositories.PaymentRepository;

import java.util.List;
import java.util.UUID;

public class ReportService {

    PaymentRepository repository;
    IDTUPayMessageQueue2 messageQueue;

    public ReportService(IDTUPayMessageQueue2 messageQueue, PaymentRepository repository) {
        this.messageQueue = messageQueue;
        this.repository = repository;
    }

    //TODO implement Event2
    public List<Payment> getPayments() throws PaymentNotFoundException {
        List<Payment> payments = repository.getPayments();
        CorrelationID correlationID = CorrelationID.randomID();
        //FullReportGenerated event = new FullReportGenerated(payments);
        System.out.println("Created FullReportGenerated event: " + correlationID);
        //messageQueue.publish(event);
        System.out.println("Published FullReportGenerated event: " + correlationID);
        return payments;
    }

    public List<Payment> getPaymentByMerchantId(UserId id) throws PaymentNotFoundException {
        List<Payment> merchantPayments = repository.getPaymentsByMerchantId(id.getUuid());
        CorrelationID correlationID = CorrelationID.randomID();
        //MerchantReportGenerated event = new MerchantReportGenerated(id, merchantPayments);
        System.out.println("Created MerchantReportGenerated event: " + correlationID);
        //messageQueue.publish(event);
        System.out.println("Published MerchantReportGenerated event: " + correlationID);
        return merchantPayments;
    }

    public List<Payment> getPaymentByCustomerId(UserId id, Token token) throws PaymentNotFoundException {
        List<Payment> customerPayments = repository.getPaymentsByToken(token);
        Event2 event = new Event2("CustomerReportGenerated", new Object[]{new CustomerReportGenerated(id, customerPayments)});
        messageQueue.publish(event);
        return customerPayments;

    }

    public void savePayment(Payment payment) {
        repository.save(payment);
    }
}
