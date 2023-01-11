package org.dtu.services;

import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;
import org.dtu.exceptions.InvalidTokenAmountException;
import org.dtu.exceptions.TokenAmountExeededException;
import org.dtu.exceptions.UserNotFoundException;
import org.dtu.repository.TokenRepository;

import java.util.ArrayList;

public class TokenService {

    private TokenRepository tokenRepository;

    public TokenService(){
        this.tokenRepository = new TokenRepository();
    }

    public ArrayList<Token> getUsedTokens(UserId userId) throws UserNotFoundException {
        return tokenRepository.getUsedTokens(userId);
    }

    public Boolean consumeToken(UserId userId, Token token){
        return tokenRepository.consumeToken(userId, token);
    }

    public ArrayList<Token> generateTokens(UserId userId, int amount) throws InvalidTokenAmountException, TokenAmountExeededException {
        ArrayList<Token> generatedTokens = tokenRepository.generateToken(userId, amount);
        return generatedTokens;
    }

}
