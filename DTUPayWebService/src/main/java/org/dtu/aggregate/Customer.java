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
public class Customer {
    Name name;
    UserId customerId;
    String bankAccountId;

    public Customer(String firstName, String lastName) {
        this.customerId = new UserId(UUID.randomUUID());
        this.name = new Name(firstName, lastName);
    }
}



