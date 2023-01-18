package org.dtu.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class Token implements Serializable {

    private UUID id;

    public Token(){
        id = UUID.randomUUID();
    }
}
