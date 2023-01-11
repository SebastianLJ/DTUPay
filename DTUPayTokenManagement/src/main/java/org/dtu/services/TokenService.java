package org.dtu.services;

import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;
import org.dtu.repository.TokenRepository;

import java.util.ArrayList;
import java.util.List;

public class TokenService {

    private TokenRepository tokenRepository;


    public TokenService(){
        this.tokenRepository = new TokenRepository();
    }


    public ArrayList<Token> getTokens(UserId userId) {
        return tokenRepository.getTokensByUserId(userId);
    }

    public Boolean consumeToken(UserId userId, Token token){
        return tokenRepository.validateToken(userId, token);
    }

    public List<Token> generateTokens(UserId userId, int amount){
        boolean status = tokenRepository.generateToken(userId, amount);
        return
    }

    public Boolean containsUser(UserId userId){
        return tokenRepository.containsUser(userId);
    }

}
