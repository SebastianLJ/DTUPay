package org.dtu.repository;

import messageUtilities.queues.IDTUPayMessageQueue;
/*import org.dtu.aggregate.Token;*/
import org.dtu.domain.Token;
import org.dtu.aggregate.UserId;
import org.dtu.event.*;

import java.io.NotSerializableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReadModelRepository {

    private HashMap<Token, UserId> tokenRepository = new HashMap<>();
    private HashMap<UserId, Integer> tokenAmountRepository = new HashMap<>();
    private HashMap<UserId, List<Token>> usedTokenRepository = new HashMap<>();

    private final IDTUPayMessageQueue messageQueue;

    public ReadModelRepository(IDTUPayMessageQueue messageQueue) {
        System.out.println("Read model init");
        this.messageQueue = messageQueue;
        messageQueue.addHandler(TokensRequested.class, e -> apply((TokensRequested) e));
        messageQueue.addHandler(ConsumeToken.class, e -> apply((ConsumeToken) e));
        messageQueue.addHandler(UserTokensRequested.class, e -> apply((UserTokensRequested) e));
    }

    private void apply(ConsumeToken event) {
        UserId userid = tokenRepository.get(event.getToken());
        tokenRepository.remove(event.getToken());
        tokenAmountRepository.put(userid, tokenAmountRepository.get(userid) - 1);

        if (usedTokenRepository.get(userid) == null){
            ArrayList<Token> newList = new ArrayList<>();
            newList.add(event.getToken());
            usedTokenRepository.put(userid, newList);
        }else{
            usedTokenRepository.get(userid).add(event.getToken());
        }

        TokenConsumed tokenConsumed = new TokenConsumed(userid);
        messageQueue.publish(tokenConsumed);
    }

    private void apply(UserTokensRequested event){
        List<Token> usedTokens = usedTokenRepository.get(event.getUserId());
        //TODO handle no list
        UserTokensGenerated userTokensGenerated = new UserTokensGenerated(event.getUserId(), usedTokens);
        messageQueue.publish(userTokensGenerated);
    }

    private void apply(TokensRequested event) {
        System.out.println("Handle tokens requested");
        ArrayList<Token> tokens = new ArrayList<>();
        if (event.getAmount() > 5 || event.getAmount() < 1) {
            TokensGenerated tokensGenerated = new TokensGenerated(event.getCorrelationID(),event.getUserId(),tokens);
            tokensGenerated.setMessage("Token amount requested is invalid");
            messageQueue.publish(tokensGenerated);
            return;
        }
        if (!tokenAmountRepository.containsKey(event.getUserId())){
            tokenAmountRepository.put(event.getUserId(),0);
        }
        Integer amount = tokenAmountRepository.get(event.getUserId());
        if (!(amount == 0 || amount == 1)){
            TokensGenerated tokensGenerated = new TokensGenerated(event.getCorrelationID(), event.getUserId(),tokens);
            tokensGenerated.setMessage("User must have either 0 or 1 token to request more tokens.");
            return;
        }
        for (int i = 0; i < event.getAmount(); i++) {
            Token token = new Token();
            tokens.add(token);
            tokenRepository.put(token, event.getUserId());
            tokenAmountRepository.put(event.getUserId(), tokenAmountRepository.get(event.getUserId()) + 1);
        }
        TokensGenerated tokensGenerated = new TokensGenerated(event.getCorrelationID(), event.getUserId(),tokens);
        messageQueue.publish(tokensGenerated);
        System.out.println("Published tokens generated");
    }

    public UserId getUserIdByToken(Token token){
        return tokenRepository.get(token);
    }

    public Integer getAmountTokensForUser(UserId userid){
        return tokenAmountRepository.get(userid);
    }

    public Integer hashmapSize(){
        return tokenRepository.size();
    }
}
