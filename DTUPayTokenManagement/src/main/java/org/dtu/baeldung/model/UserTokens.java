package org.dtu.baeldung.model;

import lombok.Data;

import java.util.*;

@Data
public class UserTokens {
    private Set<Token> UserTokens = new HashSet<>();
}
