package org.dtu.repositories;


import org.dtu.aggregate.User;
import org.dtu.exceptions.*;
import org.dtu.exceptions.InvalidMerchantIdException;
import org.dtu.exceptions.MerchantNotFoundException;

import java.util.ArrayList;
import java.util.UUID;

public class CustomerRepository {

    static ArrayList<User> customers = new ArrayList<>();

    public CustomerRepository() {

    }

    public User addCustomer(String firstName, String lastName) throws CustomerAlreadyExistsException {
        User newUser = new User(firstName, lastName);
        customers.add(newUser);
        return newUser;
    }

    public User addCustomer(User user) throws CustomerAlreadyExistsException {
        User newUser = new User(user.getName().getFirstName(), user.getName().getLastName(), user.getBankNumber());
        customers.add(newUser);
        return newUser;
    }

    public static User getCustomer (UUID id) throws InvalidCustomerIdException{
        User targetCustomer = null;
        for (User customer:
                customers)  {
            if (customer.getUserId().getUuid().equals(id)) {
                targetCustomer = customer;
                break;
            }
        }
        return targetCustomer;
    }

    public ArrayList<User> getCustomerList() {
        return new ArrayList<>(customers);
    }

    public User deleteCustomer(UUID id) throws InvalidCustomerIdException {
        User customerToRemove = getCustomer(id);
        this.customers.remove(customerToRemove);
        return customerToRemove;
    }

}
