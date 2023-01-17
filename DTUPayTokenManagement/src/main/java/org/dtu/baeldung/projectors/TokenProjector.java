package org.dtu.baeldung.projectors;

import org.dtu.baeldung.model.User;
import org.dtu.baeldung.model.UserId;
import org.dtu.baeldung.repositories.TokenReadRepository;

import java.util.Optional;

public class TokenProjector {

    TokenReadRepository tokenReadRepository = new TokenReadRepository();

    public TokenProjector(TokenReadRepository tokenReadRepository) {
        this.tokenReadRepository = tokenReadRepository;
    }

    public void project(User user){
        //UserId userId = Optional.ofNullable(tokenReadRepository.getUserId());


        //tokenReadRepository.addUserId();
    }
}
