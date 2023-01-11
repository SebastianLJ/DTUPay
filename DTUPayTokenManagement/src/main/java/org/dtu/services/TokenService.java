package org.dtu.services;

import org.dtu.aggregate.Token;
import org.dtu.repository.TokenRepository;

import java.util.List;

public class TokenService {

    TokenRepository tokenRepository;


    public TokenService(TokenRepository tokenRepository){
        this.tokenRepository = tokenRepository;

    }

    public List<Token> getTokens(){

    }

    public List<Token> createUser(){

    }

}
