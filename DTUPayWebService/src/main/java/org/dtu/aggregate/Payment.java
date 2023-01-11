package org.dtu.aggregate;

import java.util.UUID;

public class Payment {
    public UUID id;
    public UUID cid, mid;
    public int amount;
    Token token;

    public Payment() {

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
