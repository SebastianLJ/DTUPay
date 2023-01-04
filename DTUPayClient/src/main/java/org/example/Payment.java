package org.example;

public class Payment {
    public String mid;
    public String cid;
    public int amount;

    public Payment() {
    }
    public Payment(String mid, String cid, int amount) {
        this.mid = mid;
        this.cid = cid;
        this.amount = amount;
    }
    public String getMid() {
        return mid;
    }
    public String getCid() {
        return cid;
    }
    public int getAmount() {
        return amount;
    }
}
