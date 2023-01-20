package org.dtu.repositories;

import org.dtu.aggregate.Token;
import org.dtu.exceptions.PaymentNotFoundException;
import org.dtu.aggregate.Payment;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sebastian Lund (s184209)
 */
public class PaymentRepository {

    private final static PaymentRepository instance = new PaymentRepository();
    private final ArrayList<Payment> payments = new ArrayList<>();

    private PaymentRepository() { }

    public synchronized static PaymentRepository getInstance() {
        return instance;
    }

    public synchronized void save(Payment payment) {
        payments.add(payment);
    }

    public synchronized Payment getPaymentById(UUID id) throws PaymentNotFoundException {
         Optional<Payment> result = payments.stream()
                 .filter(payment -> payment.getId() == id)
                 .findAny();
         if (result.isPresent()) {
             return result.get();
         } else {
             throw new PaymentNotFoundException("Payment with id " + id + " not found");
         }
    }

    public synchronized List<Payment> getPayments() {
        return new ArrayList<>(payments);
    }

    public synchronized List<Payment> getPaymentsByMerchantId(UUID merchantId) {
        return payments.stream()
                .filter(payment -> payment.getMid().equals(merchantId))
                .collect(Collectors.toList());
    }

    public synchronized Payment getPaymentsByToken(Token token) {
        return payments.stream()
                .filter(payment -> payment.getToken().getId().equals(token.getId()))
                .collect(Collectors.toList()).get(0);
    }

    public synchronized Payment deletePayment(UUID id) throws PaymentNotFoundException {
        Payment paymentToRemove = this.getPaymentById(id);
        this.payments.remove(paymentToRemove);
        return paymentToRemove;
    }
}
