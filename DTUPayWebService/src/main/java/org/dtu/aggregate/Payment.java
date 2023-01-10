package org.dtu.aggregate;

import java.util.UUID;

public class Payment {
    public UUID id;
    public UUID cid, mid;
    public int amount;

    public Payment() {

    }

    public Payment(UUID id, UUID cid, UUID mid, int amount) {
        this.id = id;
        this.cid = cid;
        this.mid = mid;
        this.amount = amount;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
