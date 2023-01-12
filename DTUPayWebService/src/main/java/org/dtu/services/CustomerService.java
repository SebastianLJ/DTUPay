package org.dtu.services;


import org.dtu.aggregate.User;
import org.dtu.exceptions.CustomerAlreadyExistsException;
import org.dtu.exceptions.InvalidCustomerIdException;
import org.dtu.repositories.CustomerRepository;

import java.util.ArrayList;
import java.util.UUID;

public class CustomerService {
   CustomerRepository repository;

    public CustomerService() {repository = new CustomerRepository();}

    public User getCustomer (UUID id) throws InvalidCustomerIdException {
        try {
            return repository.getCustomer(id);
        } catch (InvalidCustomerIdException e) {
            throw new InvalidCustomerIdException();
        }
    }

    public User addCustomer(String firstName, String lastName) throws CustomerAlreadyExistsException {
        try {
            User newUser = repository.addCustomer(firstName, lastName);
            return newUser;
        } catch (CustomerAlreadyExistsException e) {
            throw new CustomerAlreadyExistsException();
        }
    }

    public User addCustomer(String firstName, String lastName, String bankAccount) throws CustomerAlreadyExistsException {
        try {
            User newUser = repository.addCustomer(firstName, lastName, bankAccount);
            return newUser;
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
}
