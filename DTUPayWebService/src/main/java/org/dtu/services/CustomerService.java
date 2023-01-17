package org.dtu.services;


import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;
import messageUtilities.queues.IDTUPayMessageQueue;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.User;
import org.dtu.aggregate.UserId;
import org.dtu.events.AccountDeletionRequested;
import org.dtu.events.GeneratedToken;
import org.dtu.events.TokenRequested;
import org.dtu.events.TokensDeleted;
import org.dtu.exceptions.CustomerAlreadyExistsException;
import org.dtu.exceptions.CustomerNotFoundException;
import org.dtu.exceptions.InvalidCustomerIdException;
import org.dtu.exceptions.InvalidCustomerNameException;
import org.dtu.repositories.CustomerRepository;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CustomerService {
   CustomerRepository repository;
   IDTUPayMessageQueue messageQueue;

    CompletableFuture<GeneratedToken> tokenEvent;

    CompletableFuture<UUID> deletedStudent;

    public CustomerService() {repository = new CustomerRepository();}

    public CustomerService(IDTUPayMessageQueue messageQueue) {
        this.repository = new CustomerRepository();
        this.messageQueue = messageQueue;
        this.messageQueue.addHandler(GeneratedToken.class, e -> apply((GeneratedToken) e));
        this.messageQueue.addHandler(TokensDeleted.class, e -> handleCustomerAccountDeleted((TokensDeleted) e));

    }

    public User getCustomer (UUID id) throws InvalidCustomerIdException, CustomerNotFoundException {
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
        try {
            return repository.addCustomer(user);
        } catch (CustomerAlreadyExistsException e) {
            throw new CustomerAlreadyExistsException();
        } catch (InvalidCustomerNameException e) {
            throw new InvalidCustomerNameException();
        }
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
        this.tokenEvent = new CompletableFuture<>();
        messageQueue.publish(new TokenRequested(CorrelationID.randomID(), amount, userId));
        GeneratedToken result = this.tokenEvent.join();
        return result.getTokens();
    }

    public void apply(GeneratedToken event) {
        this.tokenEvent.complete(event);
    }

    public void handleCustomerAccountDeleted(TokensDeleted event){this.deletedStudent.complete(event.getCustomerID());}
}
