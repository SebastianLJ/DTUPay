package org.dtu.repositories;


import org.dtu.aggregate.User;
import org.dtu.exceptions.*;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Noah(s184186)
 */

public class CustomerRepository {

    static ArrayList<User> customers = new ArrayList<>();

    public CustomerRepository() {

    }

    public User addCustomer(String firstName, String lastName) throws CustomerAlreadyExistsException {
        User newUser = new User(firstName, lastName);
        customers.add(newUser);
        return newUser;
    }

    public User addCustomer(User user) throws CustomerAlreadyExistsException, InvalidCustomerNameException {
        try {
            User newUser = new User(user.getName().getFirstName(), user.getName().getLastName(), user.getBankNumber());
            customers.add(newUser);
            return newUser;
        } catch (NullPointerException e) {
            throw new InvalidCustomerNameException();
        }

    }

    public static User getCustomer(UUID id) throws CustomerNotFoundException {
        for (User customer :
                customers) {
            if (customer.getUserId().getUuid().equals(id)) {
                return customer;
            }
        }
        throw new CustomerNotFoundException("Customer not found!");
    }

    public ArrayList<User> getCustomerList() {
        return new ArrayList<>(customers);
    }

    public User deleteCustomer(User customer) {
        this.customers.remove(customer);
        return customer;
    }

}
