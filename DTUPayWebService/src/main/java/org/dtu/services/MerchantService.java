package org.dtu.services;


import messageUtilities.CorrelationID;
import messageUtilities.queues.IDTUPayMessage;
import messageUtilities.queues.IDTUPayMessageQueue;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import org.dtu.aggregate.Payment;
import org.dtu.aggregate.User;
import org.dtu.domain.Token;
import org.dtu.events.ConsumeToken;
import org.dtu.events.TokenConsumed;
import org.dtu.exceptions.*;
import org.dtu.repositories.CustomerRepository;
import org.dtu.repositories.MerchantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class MerchantService {

    private final Map<CorrelationID, CompletableFuture<IDTUPayMessage>> correlations = new ConcurrentHashMap<>();
    private final MerchantRepository repository;
    private final IDTUPayMessageQueue messageQueue;

    public MerchantService(IDTUPayMessageQueue messageQueue) {
        this.repository = new MerchantRepository();
        this.messageQueue = messageQueue;
    }

    public List<User> getMerchants() {
        return repository.getMerchantList();
    }

    public User getMerchant (UUID id) throws InvalidMerchantIdException {
        try {
             return MerchantRepository.getMerchant(id);
        } catch (InvalidMerchantIdException e) {
            throw new InvalidMerchantIdException();
        }
    }

    public Payment createPayment(Payment payment) {
        ConsumeToken consumeTokenEvent = new ConsumeToken(new CorrelationID(UUID.randomUUID()), payment.getToken());
        correlations.put(consumeTokenEvent.getCorrelationID(), new CompletableFuture<>());
        messageQueue.publish(consumeTokenEvent);

        TokenConsumed consumeTokenEventResult = (TokenConsumed) correlations.get(consumeTokenEvent.getCorrelationID()).join();

        //publish event paymentRequested
        //wait for answer (token needs to be valid, and users need to be registered)
        //fetch bank account from merchant
        //if answer was valid, fetch customers bank account info



        // if cid is not in the customers list in customerservice throw InvalidCustomerIdException
        CustomerRepository customerRepository = new CustomerRepository();
        customerRepository.getCustomer(payment.getCid());
        // if mid is not in the merchants list in merchantservice throw InvalidMerchantIdException
        MerchantRepository.getMerchant(payment.getMid());

        //payment transfer

        // if payment is already in the payments list throw PaymentAlreadyExistsException
        repository.save(payment);
    }

    public User registerMerchant(String firstName, String lastName) throws MerchantAlreadyExistsException {
        try {
            return repository.addMerchant(firstName, lastName);
        } catch (MerchantAlreadyExistsException e) {
            throw new MerchantAlreadyExistsException();
        }

    }

    public User registerMerchant(String firstName, String lastName, String bankAccount) throws MerchantAlreadyExistsException {
        try {
            return repository.addMerchant(firstName, lastName, bankAccount);
        } catch (MerchantAlreadyExistsException e) {
            throw new MerchantAlreadyExistsException();
        }
    }

    public ArrayList<User> getMerchantList() {
        return repository.getMerchantList();
    }

    public User deleteMerchant(UUID id) throws PaymentNotFoundException, MerchantNotFoundException, InvalidMerchantIdException {
        return repository.deleteMerchant(id);
    }

    private void registerHandlers() {
        this.messageQueue.addHandler(TokenConsumed.class, e -> createPaymentConsumedTokenEventResult((TokenConsumed) e));
    }

    private void createPaymentConsumedTokenEventResult(TokenConsumed event) {
        try {
            correlations.get(event.getCorrelationID()).complete(event);
            correlations.remove(event.getCorrelationID());
        } catch (Exception e) {

        }
    }

}
