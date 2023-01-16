package org.dtu.baeldung.repositories;

import org.dtu.baeldung.model.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TokenRepository {
    private Map<Token, UUID> tokenToUserDatabase = new HashMap<>();
    private Map<UUID, List<Token>> userToTokensDatabase = new HashMap<>();

    public void addToken(UUID userId, Token token){
        tokenToUserDatabase.put(token, userId);

        //TODO: make a check if user is null
        userToTokensDatabase.get(userId).add(token);
    }

    public void consumeToken(Token token){
        //Getting user from token
        UUID userId = tokenToUserDatabase.get(token);

        //TODO: make a check if user is null etc...
        userToTokensDatabase.get(userId).remove(token);
    }

    public List<Token> getTokensByUserId(UUID userId){
        return userToTokensDatabase.get(userId);
    }

    public UUID getUserIdByToken(Token token){
        return tokenToUserDatabase.get(token);
    }

}
