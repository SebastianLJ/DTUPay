package org.dtu.repositories;

import org.dtu.domain.Token;
import org.dtu.exceptions.PaymentNotFoundException;
import org.dtu.aggregate.Payment;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sebastian Lund (s184209)
 */
public class PaymentRepository {

    private final ArrayList<Payment> payments = new ArrayList<>();

    public PaymentRepository() { }

    public void save(Payment payment) {
        payments.add(payment);
    }

    public Payment getPaymentById(UUID id) throws PaymentNotFoundException {
         Optional<Payment> result = payments.stream()
                 .filter(payment -> payment.getId() == id)
                 .findAny();
         if (result.isPresent()) {
             return result.get();
         } else {
             throw new PaymentNotFoundException("Payment with id " + id + " not found");
         }
    }

    public List<Payment> getPayments() {
        return new ArrayList<>(payments);
    }

    public List<Payment> getPaymentsByMerchantId(UUID merchantId) {
        return payments.stream()
                .filter(payment -> payment.getMid().equals(merchantId))
                .collect(Collectors.toList());
    }

    public Payment getPaymentsByToken(Token token) {
        return payments.stream()
                .filter(payment -> payment.getToken().equals(token))
                .collect(Collectors.toList()).get(0);
    }

    public Payment deletePayment(UUID id) throws PaymentNotFoundException {
        Payment paymentToRemove = this.getPaymentById(id);
        this.payments.remove(paymentToRemove);
        return paymentToRemove;
    }
}
