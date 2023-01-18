package org.dtu.services;


import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;
import messageUtilities.queues.IDTUPayMessageQueue;
import org.dtu.aggregate.Token;
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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CustomerService {
    CustomerRepository repository;
    IDTUPayMessageQueue messageQueue;

    HashMap<CorrelationID, CompletableFuture<TokensGenerated>> token_events = new HashMap<>();

    CompletableFuture<UUID> deletedStudent;

    public CustomerService() {
        repository = new CustomerRepository();
    }

    public CustomerService(IDTUPayMessageQueue messageQueue) {
        this.repository = new CustomerRepository();
        this.messageQueue = messageQueue;
        this.messageQueue.addHandler(TokensGenerated.class, e -> apply((TokensGenerated) e));
        this.messageQueue.addHandler(TokensDeleted.class, e -> handleCustomerAccountDeleted((TokensDeleted) e));

    }

    public User getCustomer(UUID id) throws InvalidCustomerIdException, CustomerNotFoundException {
        try {
            return repository.getCustomer(id);
        } catch (InvalidCustomerIdException e) {
            throw new InvalidCustomerIdException();
        }
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
        token_events.put(event.getCorrelationID(), new CompletableFuture<>());
        token_events.get(event.getCorrelationID()).join();
        return repository.addCustomer(user);
    }

    public ArrayList<User> getCustomerList() {
        return repository.getCustomerList();
    }

    public UUID deleteCustomer(UUID id) throws InvalidCustomerIdException {
        deletedStudent = new CompletableFuture<>();
        Event event = new AccountDeletionRequested(CorrelationID.randomID(), id);
        messageQueue.publish(event);
        UUID deletedId = deletedStudent.join();
        repository.deleteCustomer(deletedId);
        return deletedId;
    }

    public ArrayList<Token> getTokens(UserId userId, int amount) {
        CorrelationID correlationID = CorrelationID.randomID();
        TokensRequested event = new TokensRequested(correlationID, amount, userId);
        messageQueue.publish(event);
        token_events.put(correlationID, new CompletableFuture<TokensGenerated>());
        TokensGenerated result = token_events.get(correlationID).join();
        return result.getTokens();
    }

    public void apply(TokensGenerated event) {
        if (this.token_events.containsKey(event.getCorrelationID())) {
            this.token_events.get(event.getCorrelationID()).complete(event);
        }
    }

    public void handleCustomerAccountDeleted(TokensDeleted event) {
        this.deletedStudent.complete(event.getCustomerID());
    }

}
