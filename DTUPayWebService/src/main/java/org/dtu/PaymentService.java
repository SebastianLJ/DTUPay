package org.dtu;

import org.dtu.aggregate.Payment;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.UUID;

@Singleton
public class PaymentService {
    public ArrayList<Payment> getPayments() {
        return payments;
    }

    ArrayList<Payment> payments = new ArrayList<>();


    public PaymentService() {

    }

    public Payment getPayment(UUID id) throws PaymentNotFoundException {
        for (Payment payment :
                this.payments) {
            if (payment.getId().equals(id)) {
                return payment;
            }
        }
        throw new PaymentNotFoundException();
    }

    public Payment postPayment(Payment payment) throws PaymentAlreadyExistsException, InvalidMerchantIdException, InvalidCustomerIdException {
        // if cid is not in the customers list in customerservice throw InvalidCustomerIdException
        CustomerService.getCustomer(payment.cid);
        // if mid is not in the merchants list in merchantservice throw InvalidMerchantIdException
        MerchantService.getMerchant(payment.mid);
        // if payment is already in the payments list throw PaymentAlreadyExistsException
        try {
            this.getPayment(UUID.randomUUID());
            throw new PaymentAlreadyExistsException();
        } catch (PaymentNotFoundException e) {
            this.payments.add(payment);
            return payment;
        }
    }
}
