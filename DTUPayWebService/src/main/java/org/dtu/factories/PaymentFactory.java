package org.dtu.factories;

import org.dtu.services.PaymentService;
import org.dtu.repositories.PaymentRepository;

public class PaymentFactory {
    static PaymentService service = null;

    public synchronized PaymentService getService() {
        if (service == null) {
            service = new PaymentService();
        }
        return service;
    }
}
