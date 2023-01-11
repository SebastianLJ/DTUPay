package org.dtu.factories;

import org.dtu.services.MerchantService;

public class MerchantFactory {
    static MerchantService service = null;

    public synchronized MerchantService getService() {
        if (service == null) {
            service = new MerchantService();
        }
        return service;
    }
}
