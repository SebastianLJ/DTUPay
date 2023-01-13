package org.dtu.aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Token {

    UUID id;

    public Token(){
        this.id = UUID.randomUUID();
    }
}
