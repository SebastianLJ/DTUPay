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

    private ReadModelRepository readModelRepository;

    private HashMap<UserId, Integer> tokenAmountMap = new HashMap<>();

    public TokenService(IDTUPayMessageQueue2 messageQueue){
        this.tokenRepository = new TokenRepository(messageQueue);
        this.messageQueue = messageQueue;
        readModelRepository = new ReadModelRepository(messageQueue);
    }

    /*public void generateTokens(UserId userId, int amount) throws InvalidTokenAmountException, InvalidTokenAmountRequestException {
        if (amount > 5 || amount < 1) throw new InvalidTokenAmountRequestException();

        Integer tokensAmount = tokenAmountMap.get(userId);
        if (tokensAmount == null) {
            tokensAmount = 0;
        }
        if (tokensAmount > 1) throw new InvalidTokenAmountException();

        Token token = new Token();
        ArrayList<Token> tokens = token.generateTokens(userId, amount);
        for (Token generatedToken :
                tokens) {
            tokenRepository.save(generatedToken);
        }
    }*/

    public UserId getUserIdByToken(Token token){
        return readModelRepository.getUserIdByToken(token);
    }

    public Integer getAmountTokensForUser(UserId userId){
        return readModelRepository.getAmountTokensForUser(userId);
    }

    public Integer hashmapSize(){
        return readModelRepository.hashmapSize();
    }

}
