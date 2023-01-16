package org.dtu.baeldung.services;

import org.dtu.baeldung.model.Token;
import org.dtu.baeldung.repositories.TokenRepository;

import java.util.List;
import java.util.UUID;

public class TokenService {
    private TokenRepository repository;

    public TokenService(TokenRepository repository) {
        this.repository = repository;
    }

    public void generateToken(UUID userId, int amount){
        //TODO: implement constraints and exceptions
        for (int i = 0; i < amount; i++) {
            Token token = new Token();
            repository.addToken(userId, token);
        }

    }

    public void consumeToken(Token token){
        //TODO: implement constraints and exceptions
        repository.consumeToken(token);
    }

    public List<Token> getTokensByUserId(UUID userId){
        return repository.getTokensByUserId(userId);
    }

    public UUID getUserIdByToken(Token token){
        return repository.getUserIdByToken(token);
    }
}
