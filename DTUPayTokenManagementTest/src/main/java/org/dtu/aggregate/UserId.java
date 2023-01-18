package org.dtu.aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserId implements Serializable {
    UUID uuid;
}
