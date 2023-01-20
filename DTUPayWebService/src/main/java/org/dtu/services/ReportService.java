package org.dtu.services;

import messageUtilities.cqrs.CorrelationID;
import messageUtilities.MessageEvent;
import messageUtilities.queues.IDTUPayMessage;
import messageUtilities.queues.IDTUPayMessageQueue;
import org.dtu.aggregate.Payment;
import org.dtu.aggregate.UserId;
import org.dtu.domain.Token;
import org.dtu.events.UserTokensGenerated;
import org.dtu.events.UserTokensRequested;
import org.dtu.exceptions.PaymentNotFoundException;
import org.dtu.repositories.PaymentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ReportService {

    PaymentRepository repository;
    IDTUPayMessageQueue messageQueue;

    ConcurrentHashMap<CorrelationID, CompletableFuture<IDTUPayMessage>> publishedEvents = new ConcurrentHashMap<>();


    public ReportService(IDTUPayMessageQueue messageQueue, PaymentRepository repository) {
        this.messageQueue = messageQueue;
        messageQueue.addHandler("UserTokensGenerated", this::completeEvent);
        this.repository = repository;
    }

    public void completeEvent(MessageEvent e) {
        UserTokensGenerated newEvent = e.getArgument(0, UserTokensGenerated.class);
        publishedEvents.get(newEvent.getCorrelationID()).complete(newEvent);
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

    public List<Payment> getPaymentByCustomerId(UserId id) throws PaymentNotFoundException {
        UserTokensRequested userTokensRequested = new UserTokensRequested(CorrelationID.randomID(),id);
        MessageEvent event = new MessageEvent("UserTokensRequested", new Object[]{userTokensRequested});
        publishedEvents.put(userTokensRequested.getCorrelationID(), new CompletableFuture<>());
        messageQueue.publish(event);
        UserTokensGenerated userTokensGenerated = (UserTokensGenerated) publishedEvents.get(userTokensRequested.getCorrelationID()).join();
        List<Payment> customerPayments = new ArrayList<>();
        for(Token token : userTokensGenerated.getTokens()) {
            customerPayments.add(repository.getPaymentsByToken(token));
        }
        System.out.println(customerPayments.get(0).toString());
        return customerPayments;

    }



    public void savePayment(Payment payment) {
        repository.save(payment);
    }
}
