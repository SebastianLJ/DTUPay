package org.dtu.aggregate;


import messageUtilities.events.Event;
import org.dtu.events.PaymentRequested;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Payment {
    UUID id;
    UUID cid, mid;
    int amount;
    Token token;

    private List<Event> appliedEvents = new ArrayList<Event>();


    public Payment() {
        this.id = UUID.randomUUID();
    }

//    public Payment(UUID cid, UUID mid, int amount) {
//        this.id = UUID.randomUUID();
//        this.cid = cid;
//        this.mid = mid;
//        this.amount = amount;
//    }

    public Payment(Token token, UUID mid, int amount) {
        this.id = UUID.randomUUID();
        this.token = token;
        this.mid = mid;
        this.amount = amount;
    }

    public static Payment create(Token token, UUID mid, int amount) {
        Payment payment = new Payment();
        payment.id = UUID.randomUUID();
        PaymentRequested paymentRequested = new PaymentRequested(token.getId(), token.getId(), mid, amount);
        payment.appliedEvents.add(paymentRequested);
        return payment;
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
