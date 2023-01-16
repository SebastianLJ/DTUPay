package org.dtu.baeldung.repositories;

import org.dtu.baeldung.model.Token;
import org.dtu.baeldung.model.UserId;
import org.dtu.baeldung.model.UserTokens;

import java.util.*;

public class TokenReadRepository{

    private Map<Token, UserId> userId = new HashMap<>();
    private Map<UUID, UserTokens> userToken = new HashMap<>();

    public void addUserId(Token token, UserId userId){
        this.userId.put(token, userId);
    }

    public UserId getUserId(Token token){
        return this.userId.get(token);
    }

    public void addUserToken(UUID userId, UserTokens tokens){
        this.userToken.put(userId, tokens);
    }

    public UserTokens getUserTokens(UUID userId){
        return this.userToken.get(userId);
    }

}
