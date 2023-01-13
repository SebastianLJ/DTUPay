package org.dtu.factories;

import messageUtilities.queues.IDTUPayMessageQueue;
import org.dtu.services.TokenService;

public class TokenFactory {

    private static TokenService service = null;
    private final IDTUPayMessageQueue messageQueue;

    public TokenFactory(IDTUPayMessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    public synchronized TokenService getService() {
        if (service == null) {
            service = new TokenService(messageQueue);
        }
        return service;
    }
}
