package org.dtu.repository;

import messageUtilities.queues.IDTUPayMessageQueue;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;
import org.dtu.event.ConsumeToken;
import org.dtu.event.GenerateToken;
import org.dtu.event.TokenConsumed;
import org.dtu.event.TokensGenerated;

import java.util.ArrayList;
import java.util.HashMap;

public class ReadModelRepository {

    private HashMap<Token, UserId> tokenRepository = new HashMap<>();
    private HashMap<UserId, Integer> tokenAmountRepository = new HashMap<>();
    private HashMap<Token, UserId> usedTokenRepository = new HashMap<>();

    private IDTUPayMessageQueue messagequeue;

    public ReadModelRepository(IDTUPayMessageQueue messageQueue) {
        this.messagequeue = messageQueue;
        messageQueue.addHandler(GenerateToken.class, e -> apply((GenerateToken) e));
        messageQueue.addHandler(ConsumeToken.class, e -> apply((ConsumeToken) e));
    }

    private void apply(ConsumeToken event) {
        UserId userid = tokenRepository.get(event.getToken());
        tokenRepository.remove(event.getToken());
        tokenAmountRepository.put(userid, tokenAmountRepository.get(userid) - 1);
        usedTokenRepository.put(event.getToken(), userid);

        TokenConsumed tokenConsumed = new TokenConsumed(userid);
        messagequeue.publish(tokenConsumed);
    }

    private void apply(GenerateToken event) {
        ArrayList<Token> tokens = new ArrayList<>();
        if (!tokenAmountRepository.containsKey(event.getUserId())){
            tokenAmountRepository.put(event.getUserId(),0);
        }
        for (int i = 0; i < event.getAmount(); i++) {
            Token token = new Token();
            tokens.add(token);
            tokenRepository.put(token, event.getUserId());
            tokenAmountRepository.put(event.getUserId(), tokenAmountRepository.get(event.getUserId()) + 1);
        }

        TokensGenerated tokensGenerated = new TokensGenerated(event.getUserId(),tokens);
        messagequeue.publish(tokensGenerated);
    }
}
