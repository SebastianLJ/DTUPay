package org.dtu;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class Payment implements Serializable {
    UUID id;
    UUID cid, mid;
    int amount;

    public Payment() {
        this.id=UUID.randomUUID();
    }

    public Payment(UUID cid, UUID mid, int amount) {
        this.id = UUID.randomUUID();
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
