package org.dtu.services;

import messageUtilities.queues.IDTUPayMessageQueue;
import org.dtu.aggregate.UserId;
import org.dtu.repository.ReadModelRepository;

import java.util.HashMap;

public class TokenService {

    public ReadModelRepository readModelRepository;

    /**
     * @author Asama Hayder - s185099
     */
    public TokenService(IDTUPayMessageQueue messageQueue) {
/*
        this.tokenRepository = new TokenRepository(messageQueue);
*/
        /*        this.messageQueue = messageQueue;*/
        readModelRepository = new ReadModelRepository(messageQueue);
    }
}