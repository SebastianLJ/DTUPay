package org.dtu;

import org.dtu.resources.Payment;

import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
public class PaymentService {
    int maxId = 1;

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    ArrayList<Payment> payments = new ArrayList<>();


    public PaymentService() {

    }

    public Payment getPayment(int id) throws PaymentNotFoundException {
        for (Payment payment :
                this.payments) {
            if (payment.getId() == id) {
                return payment;
            }
        }
        throw new PaymentNotFoundException();
    }

    public int postPayment(Payment payment) throws PaymentAlreadyExistsException, InvalidMerchantIdException, InvalidCustomerIdException {
        // if cid is not in the customers list in customerservice throw InvalidCustomerIdException
        CustomerService.getCustomer(payment.cid);
        // if mid is not in the merchants list in merchantservice throw InvalidMerchantIdException
        MerchantService.getMerchant(payment.mid);
        // if payment is already in the payments list throw PaymentAlreadyExistsException
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
