package org.dtu.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    Name name;
    UserId userId;
    String bankNumber = "";

    public User(String firstName, String lastName) {
        this.userId = new UserId(UUID.randomUUID());
        this.name = new Name(firstName, lastName);
    }

    public User(Name name, String bankNumber) {
        this.userId = new UserId(UUID.randomUUID());
        this.name = name;
        this.bankNumber = bankNumber;
    }

    public User(String firstName, String lastName, String bankNumber) {
        this.userId = new UserId(UUID.randomUUID());
        this.name = new Name(firstName, lastName);
        this.bankNumber = bankNumber;
    }

    public String toString() {
        return name.getFirstName() + " " + name.getLastName() + "; " +  bankNumber + "; " + userId.getUuid();
    }
}
