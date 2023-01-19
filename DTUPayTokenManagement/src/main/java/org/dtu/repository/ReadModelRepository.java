package org.dtu.repository;

import messageUtilities.cqrs.events.Event2;
import messageUtilities.queues.IDTUPayMessageQueue;
/*import org.dtu.aggregate.Token;*/
import messageUtilities.queues.IDTUPayMessageQueue2;
import org.dtu.domain.Token;
import org.dtu.aggregate.UserId;
import org.dtu.event.*;

import java.io.NotSerializableException;
import java.sql.Struct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ReadModelRepository {

    private HashMap<Token, UserId> tokenRepository = new HashMap<>();
    private HashMap<UserId, Integer> tokenAmountRepository = new HashMap<>();
    private HashMap<UserId, List<Token>> usedTokenRepository = new HashMap<>();

    private ConcurrentHashMap<UUID, Boolean> processedEventsByCorrelationId = new ConcurrentHashMap<>();

    private final IDTUPayMessageQueue2 messageQueue;

    public ReadModelRepository(IDTUPayMessageQueue2 messageQueue) {
        System.out.println("Read model init");
        this.messageQueue = messageQueue;
        messageQueue.addHandler("TokensRequested", e -> {
            TokensRequested newEvent = e.getArgument(0, TokensRequested.class);
            UUID eventID = newEvent.getCorrelationID().getId();
            if (processedEventsByCorrelationId.get(eventID) == null){
                processedEventsByCorrelationId.putIfAbsent(eventID, true);
                apply(newEvent);
            }

        });
        messageQueue.addHandler("TokenVerificationRequested", e -> {
            ConsumeToken newEvent = e.getArgument(0, ConsumeToken.class);
            UUID eventID = newEvent.getCorrelationID().getId();
            if (processedEventsByCorrelationId.get(eventID) == null){
                processedEventsByCorrelationId.putIfAbsent(eventID, true);
                apply(newEvent);
            }
        });
        messageQueue.addHandler("UserTokensRequested", e -> {
            UserTokensRequested newEvent = e.getArgument(0, UserTokensRequested.class);
            UUID eventID = newEvent.getCorrelationID().getId();
            if (processedEventsByCorrelationId.get(eventID) == null){
                processedEventsByCorrelationId.putIfAbsent(eventID, true);
                apply(newEvent);
            }
        });
        messageQueue.addHandler("AccountDeletionRequested", e -> {
            AccountDeletionRequested newEvent = e.getArgument(0, AccountDeletionRequested.class);
            UUID eventID = newEvent.getCorrelationID().getId();
            if (processedEventsByCorrelationId.get(eventID) == null){
                processedEventsByCorrelationId.putIfAbsent(eventID, true);
                apply(newEvent);
            }

        });
    }

    private void apply(AccountDeletionRequested event) {
        tokenAmountRepository.remove(event.getUser().getUserId());
        tokenRepository.entrySet()
                .removeIf(entry -> entry.getValue().equals(event.getUser().getUserId()));
        TokensDeleted tokensDeleted = new TokensDeleted(event.getCorrelationID(),event.getUser());
        Event2 newEvent = new Event2("TokensDeleted", new Object[]{tokensDeleted});
        messageQueue.publish(newEvent);
    }

    public void apply(ConsumeToken event) {
        UserId userid = tokenRepository.get(event.getToken());
        if (userid == null){
            TokenConsumed tokenConsumed = new TokenConsumed(event.getCorrelationID(),null);
            tokenConsumed.setMessage("Token does not exist");
            Event2 newEvent = new Event2("TokenConsumed", new Object[]{tokenConsumed});
            messageQueue.publish(newEvent);
        }
        tokenRepository.remove(event.getToken());
        tokenAmountRepository.put(userid, tokenAmountRepository.get(userid) - 1);

        if (usedTokenRepository.get(userid) == null){
            ArrayList<Token> newList = new ArrayList<>();
            newList.add(event.getToken());
            usedTokenRepository.put(userid, newList);
        }else{
            usedTokenRepository.get(userid).add(event.getToken());
        }

        TokenConsumed tokenConsumed = new TokenConsumed(event.getCorrelationID(),userid);
        Event2 newEvent = new Event2("TokenConsumed", new Object[]{tokenConsumed});
        messageQueue.publish(newEvent);
    }

    public void apply(UserTokensRequested event){
        List<Token> usedTokens = usedTokenRepository.get(event.getUserId());
        //TODO handle no list
        UserTokensGenerated userTokensGenerated = new UserTokensGenerated(event.getCorrelationID(),event.getUserId(), usedTokens);
        Event2 newEvent = new Event2("UserTokensGenerated", new Object[]{userTokensGenerated});
        messageQueue.publish(newEvent);
    }

    public void apply(TokensRequested event) {
        System.out.println("Handle tokens requested");
        ArrayList<Token> tokens = new ArrayList<>();
        Event2 newEvent;
        if (event.getAmount() > 5 || event.getAmount() < 1) {
            TokensGenerated tokensGenerated = new TokensGenerated(event.getCorrelationID(),event.getUserId(),tokens);
            tokensGenerated.setMessage("Token amount requested is invalid");
            newEvent = new Event2("TokensGenerated", new Object[]{tokensGenerated});
            messageQueue.publish(newEvent);
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
        newEvent = new Event2("TokensGenerated", new Object[]{tokensGenerated});
        messageQueue.publish(newEvent);
        System.out.println("Published tokens generated");
    }
}
