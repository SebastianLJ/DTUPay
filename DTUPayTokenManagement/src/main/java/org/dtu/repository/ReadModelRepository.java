package org.dtu.repository;

import messageUtilities.queues.IDTUPayMessageQueue;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;
import org.dtu.event.ConsumeToken;
import org.dtu.event.GenerateToken;

import java.util.HashMap;

public class ReadModelRepository {

    private HashMap<Token, UserId> tokenRepository = new HashMap<>();
    private HashMap<UserId, Integer> tokenAmountRepository = new HashMap<>();
    private HashMap<Token, UserId> usedTokenRepository = new HashMap<>();

    public ReadModelRepository(IDTUPayMessageQueue messageQueue) {
        messageQueue.addHandler(GenerateToken.class, e -> apply((GenerateToken) e));
        messageQueue.addHandler(ConsumeToken.class, e -> apply((ConsumeToken) e));
    }

    private void apply(ConsumeToken e) {
    }

    private void apply(GenerateToken e) {
    }
}
