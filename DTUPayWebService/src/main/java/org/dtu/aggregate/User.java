package org.dtu.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    Name name;
    UserId userId;
    String bankAccountId;

    public User(String firstName, String lastName) {
        this.userId = new UserId(UUID.randomUUID());
        this.name = new Name(firstName, lastName);
    }

    public User(String firstName, String lastName, String bankAccountId) {
        this.userId = new UserId(UUID.randomUUID());
        this.name = new Name(firstName, lastName);
        this.bankAccountId = bankAccountId;
    }
}
