package org.dtu.baeldung.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.UUID;

@Value
public class Token {
    UUID tokenID;

    public Token(){
        tokenID = UUID.randomUUID();
    }
}
