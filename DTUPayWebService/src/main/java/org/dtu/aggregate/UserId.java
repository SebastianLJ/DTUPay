package org.dtu.aggregate;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class UserId {
    private UUID uuid;
}
