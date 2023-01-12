package org.dtu.services;

import messageUtilities.events.Event;
import messageUtilities.queues.IDTUPayMessageQueue;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;
import org.dtu.event.ConsumeToken;
import org.dtu.event.ConsumedToken;
import org.dtu.event.GenerateToken;
import org.dtu.event.GeneratedToken;
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
            } catch (TokenDoesNotExistException | TokenHasAlreadyBeenUsedException ex) {
                throw new RuntimeException(ex);
            }
        });
        this.messageQueue.addHandler(GenerateToken.class, e -> {
            generateTokens((GenerateToken) e);
        });
    }

    public void consumeToken(ConsumeToken event) throws TokenDoesNotExistException, TokenHasAlreadyBeenUsedException {


        UserId user = tokenRepository.consumeToken(event.getToken());

        ConsumedToken newEvent = new ConsumedToken(user.getUuid());
        messageQueue.publish(newEvent);
    }

    public void generateTokens(GenerateToken event) {

        ArrayList<Token> tokens = tokenRepository.generateTokens(event.getUserId(),event.getAmount());

        GeneratedToken newEvent = new GeneratedToken(tokens);

        messageQueue.publish(newEvent);
    }

    public UserId consumeToken(Token token) throws TokenDoesNotExistException, TokenHasAlreadyBeenUsedException {
        return tokenRepository.consumeToken(token);
    }

    public ArrayList<Token> generateTokens(UserId userId, int amount) {
        return tokenRepository.generateTokens(userId, amount);
    }

}
