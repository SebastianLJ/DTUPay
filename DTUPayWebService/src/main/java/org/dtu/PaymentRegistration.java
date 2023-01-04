package org.dtu;

import org.dtu.resources.Payment;

import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
public class PaymentRegistration {
    int maxId = 1;

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    ArrayList<Payment> payments = new ArrayList<>();

    public PaymentRegistration() {
        /*
        this.payments.add(new Payment(1, "cid1", "mid1", 20));
        this.maxId++;
        */
    }

    public Payment getPayment(int id) throws PaymentNotFoundException {
        for (Payment payment:
                this.payments) {
            if (payment.getId() == id) {
                return payment;
            }
        }
        throw new PaymentNotFoundException();
    }

    public int postPayment(Payment payment) throws PaymentAlreadyExistsException, InvalidMerchantIdException, InvalidCustomerIdException {
        if (!payment.mid.equals("mid1")) {
            throw new InvalidMerchantIdException();
        } else if (!payment.cid.equals("cid1")) {
            throw new InvalidCustomerIdException();
        }
        try {
            this.getPayment(this.maxId);
            throw new PaymentAlreadyExistsException();
        } catch (PaymentNotFoundException e) {
            payment.setId(this.maxId);
            this.payments.add(payment);
            this.maxId++;
            return payment.getId();
        }
    }
}
