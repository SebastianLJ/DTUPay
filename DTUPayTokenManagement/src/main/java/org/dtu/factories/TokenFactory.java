package org.dtu.factories;

import org.dtu.services.TokenService;

public class TokenFactory {

    static TokenService service = null;

    public synchronized TokenService getService() {
        if (service == null) {
            service = new TokenService();
        }
        return service;
    }
}
