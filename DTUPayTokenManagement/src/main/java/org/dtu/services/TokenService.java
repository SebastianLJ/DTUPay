package org.dtu.services;

import messageUtilities.queues.IDTUPayMessageQueue;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;
import org.dtu.event.ConsumeToken;
import org.dtu.event.ConsumedToken;
import org.dtu.event.GenerateToken;
import org.dtu.event.GeneratedToken;
import org.dtu.event.TokenRequested;
import org.dtu.exceptions.*;
import org.dtu.repository.TokenRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class TokenService {

    private IDTUPayMessageQueue messageQueue;
    private TokenRepository tokenRepository;

    private HashMap<Token, UserId> tokenMap = new HashMap<>();
    private HashMap<UserId, Integer> tokenAmountMap = new HashMap<>();
    private HashMap<Token, UserId> usedTokenMap = new HashMap<>();

    public TokenService(IDTUPayMessageQueue messageQueue){
        this.tokenRepository = new TokenRepository(messageQueue);
        this.messageQueue = messageQueue;
        this.messageQueue.addHandler(ConsumeToken.class, e -> {
            try {
                consumeToken((ConsumeToken) e);
            } catch (TokenDoesNotExistException | TokenHasAlreadyBeenUsedException | NoMoreValidTokensException ex) {
                throw new RuntimeException(ex);
            }
        });
        this.messageQueue.addHandler(GenerateToken.class, e -> {
            try {
                generateTokens((GenerateToken) e);
            } catch (InvalidTokenAmountException | InvalidTokenAmountRequestException ex) {
                throw new RuntimeException(ex);
            }
        });
        this.messageQueue.addHandler(TokenRequested.class, e -> {
            try {
                generateTokens((TokenRequested) e);
            } catch (InvalidTokenAmountException | InvalidTokenAmountRequestException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void consumeToken(ConsumeToken event) throws TokenDoesNotExistException, TokenHasAlreadyBeenUsedException, NoMoreValidTokensException {
        //UserId user = tokenRepository.consumeToken(event.getToken());
        //ConsumedToken newEvent = new ConsumedToken(event.getCorrelationID(), user.getUuid());
        //messageQueue.publish(newEvent);
    }

    public void generateTokens(GenerateToken event) throws InvalidTokenAmountException, InvalidTokenAmountRequestException {
        //ArrayList<Token> tokens = tokenRepository.generateTokens(event.getUserId(),event.getAmount());
        //GeneratedToken newEvent = new GeneratedToken(event.getCorrelationID(), tokens);
        //messageQueue.publish(newEvent);
    }

    public void generateTokens(TokenRequested event) throws InvalidTokenAmountException, InvalidTokenAmountRequestException {
        /*ArrayList<Token> tokens = tokenRepository.generateTokens(event.getUserId(),event.getAmount());
        GeneratedToken newEvent = new GeneratedToken(event.getCorrelationID(), tokens);
        messageQueue.publish(newEvent);

        for (int i = 0; i < tokens.size(); i++) {
            GeneratedToken newEvent = new GeneratedToken(tokens.get(i));
            messageQueue.publish(newEvent);
        }*/
    }



    public UserId consumeToken(Token token) throws TokenDoesNotExistException, TokenHasAlreadyBeenUsedException, NoMoreValidTokensException {

        //return tokenRepository.consumeToken(token);
        return null;
    }

    public void generateTokens(UserId userId, int amount) throws InvalidTokenAmountException, InvalidTokenAmountRequestException {
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
    }



}
