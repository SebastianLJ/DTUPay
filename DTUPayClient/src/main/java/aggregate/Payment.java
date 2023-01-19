package aggregate;


import lombok.Getter;
import lombok.Setter;
import messageUtilities.cqrs.events.Event;

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
