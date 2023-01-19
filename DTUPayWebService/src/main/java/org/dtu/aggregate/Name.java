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

    private static final long serialVersionUID = -3463617591378149911L;
    String firstName;
    String lastName;



}
