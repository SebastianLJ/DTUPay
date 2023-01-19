package org.dtu.services;

import messageUtilities.queues.IDTUPayMessageQueue;
/*import org.dtu.aggregate.Token;*/
import messageUtilities.queues.IDTUPayMessageQueue2;
import org.dtu.domain.Token;
import org.dtu.aggregate.UserId;
import org.dtu.exceptions.*;
import org.dtu.repository.ReadModelRepository;
import org.dtu.repository.TokenRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class TokenService {

    private IDTUPayMessageQueue2 messageQueue;
    private TokenRepository tokenRepository;

    public ReadModelRepository readModelRepository;

    private HashMap<UserId, Integer> tokenAmountMap = new HashMap<>();

    public TokenService(IDTUPayMessageQueue2 messageQueue){
/*
        this.tokenRepository = new TokenRepository(messageQueue);
*/
/*        this.messageQueue = messageQueue;*/
        readModelRepository = new ReadModelRepository(messageQueue);
    }

}
