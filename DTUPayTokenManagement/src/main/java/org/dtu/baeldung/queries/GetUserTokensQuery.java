package org.dtu.baeldung.queries;

import lombok.Data;

import java.util.UUID;

@Data
public class GetUserTokensQuery {
    private UUID userId;
}
