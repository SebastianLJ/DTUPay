package org.dtu.baeldung.queries;

import lombok.Data;
import org.dtu.baeldung.model.Token;

import java.util.UUID;

@Data
public class GetUserIdQuery {
    private Token token;
}
