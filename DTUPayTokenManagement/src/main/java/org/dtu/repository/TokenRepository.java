package org.dtu.repository;

import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;
import org.dtu.exceptions.InvalidTokenAmountException;
import org.dtu.exceptions.TokenAmountExeededException;

import java.util.*;

public class TokenRepository {

    private HashMap<UserId, ArrayList<Token>> tokenRepository = new HashMap<>();
    private HashMap<UserId, ArrayList<Token>> usedTokenRepository = new HashMap<>();

    private void createUser(UserId userId){
        tokenRepository.put(userId, new ArrayList<>());
    }

    private void save(UserId userId, Token token) {

        if (tokenRepository.get(userId).size() != 6)

        tokenRepository.get(userId).add(token);
    }

    public int getTokenAmount(UserId userId){
        return tokenRepository.get(userId).size();
    }

    public ArrayList<Token> generateToken(UserId userid, int amount) throws TokenAmountExeededException, InvalidTokenAmountException {

        ArrayList<Token> generatedTokens = new ArrayList<>();

        if (amount < 0 || amount > 5) throw new InvalidTokenAmountException(); //amount input not allowed

        if (tokenRepository.get(userid) == null) createUser(userid); //create user if not already in repository

        //can only request tokens if 1 or 0 already in possesion
        int length = tokenRepository.get(userid).size();
        if (!(length == 1 || length == 0)) throw new TokenAmountExeededException();


        for (int i = 0; i < amount; i++) {
            UUID uuid = UUID.randomUUID();
            Token token = new Token(uuid);
            save(userid, token);
            generatedTokens.add(token);
        }
        return generatedTokens;
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
