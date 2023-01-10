package org.dtu.repository;

import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;

import java.util.HashMap;
import java.util.List;

public class TokenRepository {

    private HashMap<UserId, List<Token>> tokenRepository = new HashMap<>();
}
