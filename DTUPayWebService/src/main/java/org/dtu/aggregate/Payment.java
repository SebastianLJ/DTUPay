package org.dtu.aggregate;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Payment {
     UUID id;
     UUID cid, mid;
    int amount;
    Token token;

    public Payment() {
        this.id=UUID.randomUUID();
    }

    public Payment(UUID cid, UUID mid, int amount) {
        this.id = UUID.randomUUID();
        this.cid = cid;
        this.mid = mid;
        this.amount = amount;
    }

    public Payment(Token token, UUID mid, int amount) {
        this.id = UUID.randomUUID();
        this.token = token;
        this.mid = mid;
        this.amount = amount;
    }

    public Token getToken() {
        return token;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
