package org.dtu.baeldung.model;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class User {
    private UserId userId;
    private UserTokens userTokens;
}
