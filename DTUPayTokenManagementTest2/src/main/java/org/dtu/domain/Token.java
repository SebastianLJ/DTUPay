package org.dtu.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@Data
public class Token implements Serializable {

    private UUID id;

    public Token(){
        id = UUID.randomUUID();
    }
}
