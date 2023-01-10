package org.dtu.aggregate;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Name {
    private String firstName;
    private String lastName;

    public Name(String firstname, String lastName){
        this.firstName = firstname;
        this.lastName = lastName;
    }
}
