package org.dtu.repository;

import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TokenRepository {

    private HashMap<UserId, List<Token>> tokenRepository = new HashMap<>();

    private void save(UserId userId, Token token) {

        if (tokenRepository.get(userId).size() != 6)

        tokenRepository.get(userId).add(token);
    }


}
