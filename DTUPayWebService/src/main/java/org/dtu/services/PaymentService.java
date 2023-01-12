package org.dtu.services;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import org.dtu.aggregate.Payment;
import org.dtu.exceptions.InvalidCustomerIdException;
import org.dtu.exceptions.InvalidMerchantIdException;
import org.dtu.exceptions.PaymentAlreadyExistsException;
import org.dtu.exceptions.PaymentNotFoundException;
import org.dtu.repositories.CustomerRepository;
import org.dtu.repositories.MerchantRepository;
import org.dtu.repositories.PaymentRepository;

import java.util.List;
import java.util.UUID;

public class PaymentService {
    PaymentRepository repository;

    BankService bankService = new BankServiceService().getBankServicePort();



    public List<Payment> getPayments() {
        return repository.getPayments();
    }


    public PaymentService() {
        repository = new PaymentRepository();
    }

    public Payment getPayment(UUID id) throws PaymentNotFoundException {
        try {
            return repository.getPaymentById(id);
        } catch (PaymentNotFoundException e) {
            throw new PaymentNotFoundException();
        }
    }


    public UUID createPayment(Payment payment) throws PaymentAlreadyExistsException, InvalidMerchantIdException, InvalidCustomerIdException {
        //publish event paymentRequested
        //wait for answer (token needs to be valid, and users need to be registered)
        //fetch bank account from merchant
        //if answer was valid, fetch customers bank account info



        // if cid is not in the customers list in customerservice throw InvalidCustomerIdException
        CustomerRepository.getCustomer(payment.getCid());
        // if mid is not in the merchants list in merchantservice throw InvalidMerchantIdException
        MerchantRepository.getMerchant(payment.getMid());

        //payment transfer

        // if payment is already in the payments list throw PaymentAlreadyExistsException
        Payment newPayment = Payment.create(payment.getToken(), payment.getMid(), payment.getAmount());
        repository.save(newPayment);
        return newPayment.getId();
    }

    public Payment deletePayment(UUID id) throws PaymentNotFoundException {
        return repository.deletePayment(id);
    }
}
