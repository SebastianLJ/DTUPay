package org.dtu.repository;

import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;
import org.dtu.exceptions.TokenDoesNotExistException;
import org.dtu.exceptions.TokenHasAlreadyBeenUsedException;

import java.util.*;

public class TokenRepository {

    private HashMap<Token, UserId> tokenRepository = new HashMap<>();
    private HashMap<Token, UserId> usedTokenRepository = new HashMap<>();

/*    private void createUser(UserId userId){
        tokenRepository.put(userId, new ArrayList<>());
        usedTokenRepository.put(userId, new ArrayList<>());
    }*/

/*    private void save(UserId userId, Token token) {

        if (tokenRepository.get(userId).size() != 6){

        }

        tokenRepository.get(userId).add(token);
    }*/

/*    public ArrayList<Token> getUsedTokens(UserId userId) throws UserNotFoundException {
        ArrayList<Token> usedTokens = usedTokenRepository.get(userId);
        if (usedTokens != null){
            return usedTokens;
        }else throw new UserNotFoundException();
    }*/

/*    public ArrayList<Token> getTokens(UserId userId) throws UserNotFoundException {
        ArrayList<Token> tokens = tokenRepository.get(userId);
        if (tokens != null){
            return tokens;
        }else throw new UserNotFoundException();
    }*/

/*    public ArrayList<Token> generateToken(UserId userid, int amount) throws TokenAmountExeededException, InvalidTokenAmountException {

        ArrayList<Token> generatedTokens = new ArrayList<>();

        if (amount < 0 || amount > 5) throw new InvalidTokenAmountException(); //amount input not allowed

        if (tokenRepository.get(userid) == null) createUser(userid); //create user if not already in repository

        //can only request tokens if 1 or 0 already in possession
        int length = tokenRepository.get(userid).size();
        if (!(length == 1 || length == 0)) throw new TokenAmountExeededException();


        for (int i = 0; i < amount; i++) {
            UUID uuid = UUID.randomUUID();
            Token token = new Token(uuid);
            save(userid, token);
            generatedTokens.add(token);
        }
        return generatedTokens;
    }*/

    public ArrayList<Token> generateTokens(UserId userid, int amount) {
        ArrayList<Token> tokens = new ArrayList<>();
        for (int i = 0 ; i < amount ; i++){
            Token token = new Token(UUID.randomUUID());
            tokenRepository.put(token,userid);
            tokens.add(token);
        }
        return tokens;
    }

/*    public Boolean consumeToken(UserId userid, Token token) {
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
    }*/

    public UserId consumeToken(Token token) throws TokenDoesNotExistException, TokenHasAlreadyBeenUsedException {
        if (usedTokenRepository.containsKey(token)) throw new TokenHasAlreadyBeenUsedException();
        UserId user = tokenRepository.get(token);
        if (user == null) throw new TokenDoesNotExistException();
        usedTokenRepository.put(token, user);
        tokenRepository.remove(token);
        return user;
    }

}
