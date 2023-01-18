package org.dtu.services;


import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;
import messageUtilities.queues.IDTUPayMessageQueue;
import org.dtu.domain.Token;
import org.dtu.aggregate.User;
import org.dtu.aggregate.UserId;
import org.dtu.events.*;
import org.dtu.exceptions.CustomerAlreadyExistsException;
import org.dtu.exceptions.CustomerNotFoundException;
import org.dtu.exceptions.InvalidCustomerIdException;
import org.dtu.exceptions.InvalidCustomerNameException;
import org.dtu.repositories.CustomerRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CustomerService {
    CustomerRepository repository;
    IDTUPayMessageQueue messageQueue;

    HashMap<CorrelationID, CompletableFuture<TokensGenerated>> token_events = new HashMap<>();

    CompletableFuture<User> deletedCustomer;

    public CustomerService() {
        repository = new CustomerRepository();
    }

    public CustomerService(IDTUPayMessageQueue messageQueue, CustomerRepository repository) {
        this.repository = repository;
        this.messageQueue = messageQueue;
        this.messageQueue.addHandler(TokensGenerated.class, e -> handleTokensGenerated((TokensGenerated) e));
        this.messageQueue.addHandler(TokensDeleted.class, e -> handleTokensDeleted((TokensDeleted) e));

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
        Event event = new CustomerAccountCreated(user);
        messageQueue.publish(event);
        /*token_events.put(event.getCorrelationID(), new CompletableFuture<>());
        token_events.get(event.getCorrelationID()).join();*/
        return repository.addCustomer(user);
    }

    public ArrayList<User> getCustomerList() {
        return repository.getCustomerList();
    }

    public User deleteCustomer(User user) throws CustomerNotFoundException {
        getCustomer(user.getUserId().getUuid());
        deletedCustomer = new CompletableFuture<>();
        Event event = new AccountDeletionRequested(CorrelationID.randomID(), user);
        messageQueue.publish(event);
        User customer = deletedCustomer.join();
        repository.deleteCustomer(customer);
        return customer;
    }

    public ArrayList<Token> getTokens(UserId userId, int amount) {
        CorrelationID correlationID = CorrelationID.randomID();
        TokensRequested event = new TokensRequested(correlationID, amount, userId);
        System.out.println("Created TokensRequested event: " + correlationID);
        messageQueue.publish(event);
        System.out.println("Published TokensRequested event: " + correlationID);
        token_events.put(correlationID, new CompletableFuture<TokensGenerated>());
        TokensGenerated result = token_events.get(correlationID).join();
        return result.getTokens();
    }

    public void handleTokensGenerated(TokensGenerated event) {
        System.out.println("Received TokensGenerated event: " + event.getCorrelationID());
        if (this.token_events.containsKey(event.getCorrelationID())) {
            this.token_events.get(event.getCorrelationID()).complete(event);
        }
    }

    public void handleTokensDeleted(TokensDeleted event) {
        this.deletedCustomer.complete(event.getUser());
    }

}
