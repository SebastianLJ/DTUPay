package org.dtu.services;


import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;
import messageUtilities.cqrs.events.Event2;
import messageUtilities.queues.IDTUPayMessageQueue;
import messageUtilities.queues.IDTUPayMessageQueue2;
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
    IDTUPayMessageQueue2 messageQueue;

    HashMap<CorrelationID, CompletableFuture<TokensGenerated>> token_events = new HashMap<>();

    CompletableFuture<User> deletedCustomer;

    public CustomerService() {
        repository = new CustomerRepository();
    }

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
        System.out.println("Adding customer");
        Event2 event = new Event2("CustomerAccountCreated", new Object[]{new CustomerAccountCreated(user)});
        messageQueue.publish(event);
        System.out.println("Customer added");
        return repository.addCustomer(user);
    }

    public ArrayList<User> getCustomerList() {
        return repository.getCustomerList();
    }

    public User deleteCustomer(User user) throws CustomerNotFoundException {
        getCustomer(user.getUserId().getUuid());
        repository.deleteCustomer(user);
        deletedCustomer = new CompletableFuture<>();
        Event2 event = new Event2("AccountDeletionRequested", new Object[]{new AccountDeletionRequested(CorrelationID.randomID(), user)});
        messageQueue.publish(event);
        deletedCustomer.join();
        return user;
    }

    public ArrayList<Token> getTokens(UserId userId, int amount) {
        CorrelationID correlationID = CorrelationID.randomID();
        System.out.println("Created TokensRequested event: " + correlationID);
        Event2 event = new Event2("TokensRequested", new Object[]{new TokensRequested(correlationID, amount, userId)});
        messageQueue.publish(event);
        System.out.println("Published TokensRequested event: " + correlationID);
        token_events.put(correlationID, new CompletableFuture<TokensGenerated>());
        TokensGenerated result = token_events.get(correlationID).join();
        return result.getTokens();
    }

    public void handleTokensGenerated(Event2 event) {
        TokensGenerated newEvent = event.getArgument(0, TokensGenerated.class);
        System.out.println("Received TokensGenerated event: " + newEvent.getCorrelationID());
        if (this.token_events.containsKey(newEvent.getCorrelationID())) {
            this.token_events.get(newEvent.getCorrelationID()).complete(newEvent);
        }
    }

    public void handleTokensDeleted(Event2 event) {
        TokensDeleted newEvent = event.getArgument(0, TokensDeleted.class);
        this.deletedCustomer.complete(newEvent.getUser());
    }

}
