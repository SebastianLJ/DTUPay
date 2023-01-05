package org.dtu;

import java.util.ArrayList;

public class CustomerService {
    //contains a list of all customers
    static ArrayList<User> customers = new ArrayList<>();
    public ArrayList<User> getCustomers() {
        return customers;
    }

    public CustomerService() {

    }

    public static User getCustomer (String id) throws InvalidCustomerIdException {
        for (User customer:
                customers)  {
            if (customer.getId().equals(id)) {
                return customer;
            }
        }
        throw new InvalidCustomerIdException();
    }

    public void addCustomer(String id) throws CustomerAlreadyExistsException {
        //if customer already exists, throw CustomerAlreadyExists exception
        try {
            getCustomer(id);
            throw new CustomerAlreadyExistsException();
        } catch (InvalidCustomerIdException e) {
            User customer = new User(id);
            customers.add(customer);
        }

        User newUser = new User(id);
        customers.add(newUser);
    }

}
