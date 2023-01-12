package org.dtu.repository;

import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;
import org.dtu.exceptions.TokenDoesNotExistException;
import org.dtu.exceptions.TokenHasAlreadyBeenUsedException;

import java.util.*;

public class TokenRepository {

    private HashMap<Token, UserId> tokenRepository = new HashMap<>();
    private HashMap<Token, UserId> usedTokenRepository = new HashMap<>();

    public ArrayList<Token> generateTokens(UserId userid, int amount) {
        ArrayList<Token> tokens = new ArrayList<>();
        for (int i = 0 ; i < amount ; i++){
            Token token = new Token(UUID.randomUUID());
            tokenRepository.put(token,userid);
            tokens.add(token);
        }
        return tokens;
    }

    public UserId consumeToken(Token token) throws TokenDoesNotExistException, TokenHasAlreadyBeenUsedException {
        if (usedTokenRepository.containsKey(token)) throw new TokenHasAlreadyBeenUsedException();
        UserId user = tokenRepository.get(token);
        if (user == null) throw new TokenDoesNotExistException();
        usedTokenRepository.put(token, user);
        tokenRepository.remove(token);
        return user;
    }

}
