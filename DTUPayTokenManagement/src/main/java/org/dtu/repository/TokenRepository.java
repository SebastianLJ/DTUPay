package org.dtu.repository;

import messageUtilities.queues.IDTUPayMessageQueue;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;
import org.dtu.exceptions.*;

import java.util.*;

public class TokenRepository {

    private EventStore eventStore;

    public TokenRepository(IDTUPayMessageQueue bus) {
        eventStore = new EventStore(bus);
    }




    private HashMap<Token, UserId> tokenRepository = new HashMap<>();
    private HashMap<UserId, Integer> tokenAmountRepository = new HashMap<UserId, Integer>();
    private HashMap<Token, UserId> usedTokenRepository = new HashMap<>();

    public ArrayList<Token> generateTokens(UserId userid, int amount) throws InvalidTokenAmountException, InvalidTokenAmountRequestException {
        if (amount > 5 || amount < 1) throw new InvalidTokenAmountRequestException();

        Integer tokensAmount = tokenAmountRepository.get(userid);
        if (tokensAmount == null) {
            tokensAmount = 0;
        }
        if (tokensAmount > 1) throw new InvalidTokenAmountException();
        ArrayList<Token> tokens = new ArrayList<>();
        for (int i = 0 ; i < amount ; i++){
            Token token = new Token();
            tokenRepository.put(token,userid);
            tokens.add(token);
        }
        tokenAmountRepository.put(userid, tokensAmount + tokens.size());
        return tokens;
    }

    public UserId consumeToken(Token token) throws TokenDoesNotExistException, TokenHasAlreadyBeenUsedException, NoMoreValidTokensException {
        if (usedTokenRepository.containsKey(token)) throw new TokenHasAlreadyBeenUsedException();
        UserId user = tokenRepository.get(token);
        if (user == null) throw new TokenDoesNotExistException();
        if (tokenAmountRepository.get(user) == 0) throw new NoMoreValidTokensException();
        usedTokenRepository.put(token, user);
        tokenRepository.remove(token);
        tokenAmountRepository.put(user, tokenAmountRepository.get(user) - 1);
        return user;
    }

}
