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
public class Merchant {
    Name name;
    UserId merchantId;
    String bankAccountId;

    public Merchant(String firstName, String lastName) {
        this.merchantId = new UserId(UUID.randomUUID());
        this.name = new Name(firstName, lastName);
    }
}

