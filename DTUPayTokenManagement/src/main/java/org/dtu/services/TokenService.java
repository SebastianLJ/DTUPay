package org.dtu.services;

import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;
import org.dtu.exceptions.InvalidTokenAmountException;
import org.dtu.exceptions.TokenAmountExeededException;
import org.dtu.repository.TokenRepository;

import java.util.ArrayList;

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

    public ArrayList<Token> generateTokens(UserId userId, int amount) throws TokenAmountExeededException, InvalidTokenAmountException {
        return tokenRepository.generateToken(userId, amount);
    }

    public Boolean containsUser(UserId userId){
        return tokenRepository.containsUser(userId);
    }

}
