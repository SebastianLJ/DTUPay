package org.dtu;

import java.util.UUID;

public class Payment {
    public UUID mid;
    public UUID cid;
    public int amount;

    public Payment() {
    }
    public Payment(UUID mid, UUID cid, int amount) {
        this.mid = mid;
        this.cid = cid;
        this.amount = amount;
    }
    public UUID getMid() {
        return mid;
    }
    public UUID getCid() {
        return cid;
    }
    public int getAmount() {
        return amount;
    }
}
