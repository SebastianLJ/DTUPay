package org.dtu.factories;

import org.dtu.services.CustomerService;

public class CustomerFactory {
    static CustomerService service = null;

    public synchronized CustomerService getService() {
        if (service == null) {
            service = new CustomerService();
        }
        return service;
    }
}
