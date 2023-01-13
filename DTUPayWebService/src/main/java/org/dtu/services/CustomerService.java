package org.dtu.services;


import messageUtilities.queues.IDTUPayMessageQueue;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.User;
import org.dtu.aggregate.UserId;
import org.dtu.events.GeneratedToken;
import org.dtu.events.TokenRequested;
import org.dtu.exceptions.CustomerAlreadyExistsException;
import org.dtu.exceptions.InvalidCustomerIdException;
import org.dtu.repositories.CustomerRepository;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CustomerService {
   CustomerRepository repository;
   IDTUPayMessageQueue messageQueue;

    CompletableFuture<GeneratedToken> tokenEvent;

    public CustomerService() {repository = new CustomerRepository();}

    public CustomerService(IDTUPayMessageQueue messageQueue) {
        this.repository = new CustomerRepository();
        this.messageQueue = messageQueue;
        this.messageQueue.addHandler(GeneratedToken.class, e -> apply((GeneratedToken) e));

    }

    public User getCustomer (UUID id) throws InvalidCustomerIdException {
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

    public User addCustomer(User user) throws CustomerAlreadyExistsException {
        try {
            return repository.addCustomer(user);
        } catch (CustomerAlreadyExistsException e) {
            throw new CustomerAlreadyExistsException();
        }
    }

    public ArrayList<User> getCustomerList() {
        return repository.getCustomerList();
    }

    public User deleteCustomer(UUID id) throws InvalidCustomerIdException {
        return repository.deleteCustomer(id);
    }

    public ArrayList<Token> getTokens(UserId userId, int amount) {
        this.tokenEvent = new CompletableFuture<>();
        messageQueue.publish(new TokenRequested(amount, userId));
        GeneratedToken result = this.tokenEvent.join();
        return result.getTokens();
    }

    public void apply(GeneratedToken event) {
        this.tokenEvent.complete(event);
    }
}
