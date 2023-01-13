package org.dtu.aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Token implements Serializable {

    UUID id;
}
