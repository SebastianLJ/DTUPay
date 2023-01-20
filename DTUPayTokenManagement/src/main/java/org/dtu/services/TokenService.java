package org.dtu.services;

import messageUtilities.queues.IDTUPayMessageQueue2;
import org.dtu.aggregate.UserId;
import org.dtu.repository.ReadModelRepository;

import java.util.HashMap;

public class TokenService {

    public ReadModelRepository readModelRepository;

    private HashMap<UserId, Integer> tokenAmountMap = new HashMap<>();

    /**
     * @author Asama Hayder - s185099
     */
    public TokenService(IDTUPayMessageQueue2 messageQueue) {
/*
        this.tokenRepository = new TokenRepository(messageQueue);
*/
        /*        this.messageQueue = messageQueue;*/
        readModelRepository = new ReadModelRepository(messageQueue);
    }
}