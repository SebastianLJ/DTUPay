package org.dtu.services;

import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;
import org.dtu.exceptions.*;
import org.dtu.repository.TokenRepository;

import java.util.ArrayList;

public class TokenService {

    private TokenRepository tokenRepository;

    public TokenService(){
        this.tokenRepository = new TokenRepository();
    }

    public UserId consumeToken(Token token) throws TokenDoesNotExistException, TokenHasAlreadyBeenUsedException {
        return tokenRepository.consumeToken(token);
    }

    public ArrayList<Token> generateTokens(UserId userId, int amount) {
        return tokenRepository.generateTokens(userId, amount);
    }

}
