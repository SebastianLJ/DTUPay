package org.dtu;

import org.dtu.aggregate.User;

import java.util.ArrayList;
import java.util.UUID;

public class CustomerService {
    //contains a list of all customers
    static ArrayList<User> customers = new ArrayList<>();
    public ArrayList<User> getCustomers() {
        return customers;
    }

    public CustomerService() {

    }

    public static User getCustomer (UUID id) throws InvalidCustomerIdException {
        for (User customer:
                customers)  {
            if (customer.getUserId().getUuid().equals(id)) {
                return customer;
            }
        }
        throw new InvalidCustomerIdException();
    }

    public void addCustomer(String firstName, String lastName) throws CustomerAlreadyExistsException {
        User newUser = new User(firstName, lastName);
        customers.add(newUser);
    }

}
