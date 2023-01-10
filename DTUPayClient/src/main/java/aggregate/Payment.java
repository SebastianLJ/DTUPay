package aggregate;

import java.util.UUID;

public class Payment {
    public int id;
    public UUID cid, mid;
    public int amount;

    public Payment() {

    }

    public Payment(int id, UUID cid, UUID mid, int amount) {
        this.id = id;
        this.cid = cid;
        this.mid = mid;
        this.amount = amount;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
