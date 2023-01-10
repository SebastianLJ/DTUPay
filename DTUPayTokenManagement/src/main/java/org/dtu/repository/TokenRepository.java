package org.dtu.repository;

import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TokenRepository {

    private HashMap<UserId, List<Token>> tokenRepository = new HashMap<>();
    private HashMap<UserId, List<Token>> usedTokenRepository = new HashMap<>();

    private void save(UserId userId, Token token) {

        if (tokenRepository.get(userId).size() != 6)

        tokenRepository.get(userId).add(token);
    }


    private Token generateToken(UserId userid){
        UUID uuid = UUID.randomUUID();
        Token token = new Token(uuid);
        save(userid, token);
        return token;
    }

    private Boolean validateToken(UserId userid, Token token) {
        List<Token> tokens = tokenRepository.get(userid);
        for (int i = 0 ; i<tokens.toArray().length ; i++){
            if (tokens.get(i).getUuid().compareTo(token.getUuid()) == 0){

                return true;
            }

        }
        return false;
    }


}