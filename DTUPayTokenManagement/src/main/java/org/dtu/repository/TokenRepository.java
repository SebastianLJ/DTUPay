package org.dtu.repository;

import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TokenRepository {

    private HashMap<UserId, ArrayList<Token>> tokenRepository = new HashMap<>();
    private HashMap<UserId, ArrayList<Token>> usedTokenRepository = new HashMap<>();

    public ArrayList<Token> createUser(UserId userId){
        ArrayList<Token> tokenArrayList = new ArrayList<Token>();

        while(tokenArrayList.size() < 6){
            tokenArrayList.add(new Token(UUID.randomUUID()));
        }

        return tokenArrayList;

    }
    private void save(UserId userId, Token token) {

        if (tokenRepository.get(userId).size() != 6)

        tokenRepository.get(userId).add(token);
    }

    public int getTokenAmount(UserId userId){
        return tokenRepository.get(userId).size();
    }

    public Token generateToken(UserId userid){
        UUID uuid = UUID.randomUUID();
        Token token = new Token(uuid);
        save(userid, token);
        return token;
    }

    public Boolean validateToken(UserId userid, Token token) {
        ArrayList<Token> tokens = tokenRepository.get(userid);
        for (int i = 0 ; i<tokens.toArray().length ; i++){
            if (tokens.get(i).getId().compareTo(token.getId()) == 0){
                tokens.remove(i);
                tokenRepository.replace(userid, tokens);
                usedTokenRepository.get(userid).add(token);
                return true;
            }
        }
        return false;
    }

    public Boolean containsUser(UserId userId){
        return tokenRepository.containsKey(userId);
    }

    public ArrayList<Token> getTokensByUserId(UserId userId){
        return tokenRepository.get(userId);
    }




}
