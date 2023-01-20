package org.dtu.aggregate;


import messageUtilities.cqrs.CorrelationID;
import messageUtilities.cqrs.events.Event;
import org.dtu.domain.Token;
import org.dtu.events.PaymentRequested;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Payment implements Serializable {
    private static final long serialVersionUID = -4831808956822934219L;
    UUID id;
    UUID mid;
    int amount;
    Token token;

    private List<Event> appliedEvents = new ArrayList<Event>();


    public Payment() {
        this.id = UUID.randomUUID();
    }

    public Payment(Token token, UUID mid, int amount) {
        this.id = UUID.randomUUID();
        this.token = token;
        this.mid = mid;
        this.amount = amount;
    }

    public static Payment create(Token token, UUID mid, int amount) {
        Payment payment = new Payment();
        payment.id = UUID.randomUUID();
        PaymentRequested paymentRequested = new PaymentRequested(CorrelationID.randomID(), token.getId(), token.getId(), mid, amount);
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
