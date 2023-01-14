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

public class TokenService {

    private IDTUPayMessageQueue messageQueue;
    private TokenRepository tokenRepository;

    public TokenService(IDTUPayMessageQueue messageQueue){
        this.tokenRepository = new TokenRepository();
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
        UserId user = tokenRepository.consumeToken(event.getToken());
        ConsumedToken newEvent = new ConsumedToken(event.getCorrelationID(), user.getUuid());
        messageQueue.publish(newEvent);
    }

    public void generateTokens(GenerateToken event) throws InvalidTokenAmountException, InvalidTokenAmountRequestException {
        ArrayList<Token> tokens = tokenRepository.generateTokens(event.getUserId(),event.getAmount());
        GeneratedToken newEvent = new GeneratedToken(event.getCorrelationID(), tokens);
        messageQueue.publish(newEvent);
    }

    public void generateTokens(TokenRequested event) throws InvalidTokenAmountException, InvalidTokenAmountRequestException {
        ArrayList<Token> tokens = tokenRepository.generateTokens(event.getUserId(),event.getAmount());
        GeneratedToken newEvent = new GeneratedToken(event.getCorrelationID(), tokens);
        messageQueue.publish(newEvent);
    }

    public UserId consumeToken(Token token) throws TokenDoesNotExistException, TokenHasAlreadyBeenUsedException, NoMoreValidTokensException {
        return tokenRepository.consumeToken(token);
    }

    public ArrayList<Token> generateTokens(UserId userId, int amount) throws InvalidTokenAmountException, InvalidTokenAmountRequestException {
        return tokenRepository.generateTokens(userId, amount);
    }

}
