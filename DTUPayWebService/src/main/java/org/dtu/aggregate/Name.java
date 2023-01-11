package org.dtu.aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Name implements Serializable {

    String firstName;
    String lastName;



}
