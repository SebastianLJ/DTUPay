package org.dtu.baeldung.projections;

import org.dtu.baeldung.model.UserId;
import org.dtu.baeldung.model.UserTokens;
import org.dtu.baeldung.queries.GetUserIdQuery;
import org.dtu.baeldung.queries.GetUserTokensQuery;
import org.dtu.baeldung.repositories.TokenReadRepository;

import java.util.UUID;

public class TokenProjection {
    TokenReadRepository tokenReadRepository;

    public TokenProjection(TokenReadRepository tokenReadRepository) {
        this.tokenReadRepository = tokenReadRepository;
    }

    public UserId handle(GetUserIdQuery query){
        return tokenReadRepository.getUserId(query.getToken());
    }

    public UserTokens handle(GetUserTokensQuery query){
        return tokenReadRepository.getUserTokens(query.getUserId());
    }
}
