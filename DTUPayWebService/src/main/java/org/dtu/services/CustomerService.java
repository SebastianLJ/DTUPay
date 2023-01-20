package org.dtu.services;


import messageUtilities.cqrs.CorrelationID;
import messageUtilities.MessageEvent;
import messageUtilities.queues.IDTUPayMessageQueue2;
import org.dtu.domain.Token;
import org.dtu.aggregate.User;
import org.dtu.aggregate.UserId;
import org.dtu.events.*;
import org.dtu.exceptions.CustomerAlreadyExistsException;
import org.dtu.exceptions.CustomerNotFoundException;
import org.dtu.exceptions.InvalidCustomerNameException;
import org.dtu.repositories.CustomerRepository;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
@author Noah (s184186)
 */

public class CustomerService {
    CustomerRepository repository;
    IDTUPayMessageQueue2 messageQueue;

    ConcurrentHashMap<CorrelationID, CompletableFuture<TokensGenerated>> token_events = new ConcurrentHashMap<>();

    CompletableFuture<User> deletedCustomer;

    public CustomerService() {
        repository = new CustomerRepository();
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public CustomerService(IDTUPayMessageQueue2 messageQueue, CustomerRepository repository) {
        this.repository = repository;
        this.messageQueue = messageQueue;
        this.messageQueue.addHandler("TokensGenerated", this::handleTokensGenerated);
        this.messageQueue.addHandler("TokensDeleted", this::handleTokensDeleted);

    }

    public User getCustomer(UUID id) throws CustomerNotFoundException {
        return repository.getCustomer(id);
    }

    public User addCustomer(String firstName, String lastName) throws CustomerAlreadyExistsException {
        try {
            return repository.addCustomer(firstName, lastName);
        } catch (CustomerAlreadyExistsException e) {
            throw new CustomerAlreadyExistsException();
        }
    }

    public User addCustomer(User user) throws CustomerAlreadyExistsException, InvalidCustomerNameException {
        MessageEvent event = new MessageEvent("CustomerAccountCreated", new Object[]{new CustomerAccountCreated(user)});
        messageQueue.publish(event);
        return repository.addCustomer(user);
    }

    public ArrayList<User> getCustomerList() {
        return repository.getCustomerList();
    }

    public User deleteCustomer(User user) throws CustomerNotFoundException {
        getCustomer(user.getUserId().getUuid());
        repository.deleteCustomer(user);
        deletedCustomer = new CompletableFuture<>();
        MessageEvent event = new MessageEvent("AccountDeletionRequested", new Object[]{new AccountDeletionRequested(CorrelationID.randomID(), user)});
        messageQueue.publish(event);
        deletedCustomer.join();
        return user;
    }

    /**
     * @author Sebastian Lund (s184209)
     */
    public ArrayList<Token> getTokens(UserId userId, int amount) {
        CorrelationID correlationID = CorrelationID.randomID();
        MessageEvent event = new MessageEvent("TokensRequested", new Object[]{new TokensRequested(correlationID, amount, userId)});
        messageQueue.publish(event);
        token_events.put(correlationID, new CompletableFuture<TokensGenerated>());
        TokensGenerated result = token_events.get(correlationID).join();
        return result.getTokens();
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public void handleTokensGenerated(MessageEvent event) {
        TokensGenerated newEvent = event.getArgument(0, TokensGenerated.class);
        System.out.println("Received TokensGenerated event: " + newEvent.getCorrelationID());
        if (this.token_events.containsKey(newEvent.getCorrelationID())) {
            this.token_events.get(newEvent.getCorrelationID()).complete(newEvent);
        }
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public void handleTokensDeleted(MessageEvent event) {
        TokensDeleted newEvent = event.getArgument(0, TokensDeleted.class);
        this.deletedCustomer.complete(newEvent.getUser());
    }

}
