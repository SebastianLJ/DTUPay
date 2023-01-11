package org.dtu.factories;

import org.dtu.services.PaymentService;

public class PaymentFactory {
    static PaymentService service = null;

    public synchronized PaymentService getService() {
        if (service == null) {
            service = new PaymentService();
        }
        return service;
    }
}
