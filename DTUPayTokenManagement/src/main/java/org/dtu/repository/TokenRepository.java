package org.dtu.repository;

import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;
import org.dtu.exceptions.InvalidTokenAmountException;
import org.dtu.exceptions.TokenDoesNotExistException;
import org.dtu.exceptions.TokenHasAlreadyBeenUsedException;

import java.util.*;

public class TokenRepository {

    private HashMap<Token, UserId> tokenRepository = new HashMap<>();
    private HashMap<UserId, Integer> tokenAmountRepository = new HashMap<UserId, Integer>();
    private HashMap<Token, UserId> usedTokenRepository = new HashMap<>();

    public ArrayList<Token> generateTokens(UserId userid, int amount) throws InvalidTokenAmountException {
        int tokensAmount = tokenAmountRepository.get(userid);
        if (tokensAmount > 1 || tokensAmount < 0) throw new InvalidTokenAmountException();
        ArrayList<Token> tokens = new ArrayList<>();
        for (int i = 0 ; i < amount ; i++){
            Token token = new Token();
            tokenRepository.put(token,userid);
            tokens.add(token);
        }
        tokenAmountRepository.put(userid, tokens.size());
        return tokens;
    }

    public UserId consumeToken(Token token) throws TokenDoesNotExistException, TokenHasAlreadyBeenUsedException {
        if (usedTokenRepository.containsKey(token)) throw new TokenHasAlreadyBeenUsedException();
        UserId user = tokenRepository.get(token);
        if (user == null) throw new TokenDoesNotExistException();
        usedTokenRepository.put(token, user);
        tokenRepository.remove(token);
        tokenAmountRepository.put(user, tokenAmountRepository.get(user) - 1);
        return user;
    }

}
