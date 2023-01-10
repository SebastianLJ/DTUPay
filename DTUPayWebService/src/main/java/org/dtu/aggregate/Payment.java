package org.dtu.aggregate;

public class Payment {
    public int id;
    public String cid, mid;
    public int amount;

    public Payment() {

    }

    public Payment(int id, String cid, String mid, int amount) {
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
