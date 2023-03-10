package org.dtu.aggregate;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor
public class TokenId {
    UUID uuid;
}
