package org.dtu.services;

import org.dtu.*;
import org.dtu.aggregate.Payment;
import org.dtu.exceptions.InvalidCustomerIdException;
import org.dtu.exceptions.InvalidMerchantIdException;
import org.dtu.exceptions.PaymentAlreadyExistsException;
import org.dtu.exceptions.PaymentNotFoundException;
import org.dtu.repositories.PaymentRepository;

import java.util.ArrayList;
import java.util.UUID;

public class PaymentService {
    PaymentRepository repository;

    public PaymentService() {
        repository = new PaymentRepository();
    }

    public ArrayList<Payment> getPayments() {
        return repository.getPayments();
    }

    public Payment getPayment(UUID id) throws PaymentNotFoundException {
        return repository.getPaymentById(id);
    }

    public Payment createPayment(Payment payment) throws PaymentAlreadyExistsException, InvalidMerchantIdException, InvalidCustomerIdException {
        // if cid is not in the customers list in customerservice throw InvalidCustomerIdException
        CustomerService.getCustomer(payment.cid);
        // if mid is not in the merchants list in merchantservice throw InvalidMerchantIdException
        MerchantService.getMerchant(payment.mid);
        // if payment is already in the payments list throw PaymentAlreadyExistsException
        try {
            this.getPayment(payment.getId());
            throw new PaymentAlreadyExistsException();
        } catch (PaymentNotFoundException e) {
            repository.save(payment);
            return payment;
        }
    }

    public Payment deletePayment(UUID id) throws PaymentNotFoundException {
        return repository.deletePayment(id);
    }
}
