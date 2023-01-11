package org.dtu.repositories;

import org.dtu.exceptions.PaymentNotFoundException;
import org.dtu.aggregate.Payment;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class PaymentRepository {
    private ArrayList<Payment> payments = new ArrayList<>();

    public PaymentRepository() {

    }

    public void save(Payment payment) {
        payments.add(payment);
    }

    public Payment getPaymentById(UUID id) throws PaymentNotFoundException {
         Optional<Payment> result = payments.stream()
                 .filter(payment -> payment.id == id)
                 .findAny();
         if (result.isPresent()) {
             return result.get();
         } else {
             throw new PaymentNotFoundException("Payment with id " + id + " not found");
         }
    }

    public ArrayList<Payment> getPayments() {
        return new ArrayList<>(payments);
    }

    public Payment deletePayment(UUID id) throws PaymentNotFoundException {
        Payment paymentToRemove = this.getPaymentById(id);
        this.payments.remove(paymentToRemove);
        return paymentToRemove;

    }
}
