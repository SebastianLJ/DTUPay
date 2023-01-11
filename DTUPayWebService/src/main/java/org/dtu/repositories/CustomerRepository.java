package org.dtu.repositories;

import org.dtu.CustomerAlreadyExistsException;
import org.dtu.InvalidCustomerIdException;
import org.dtu.aggregate.User;

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
}
