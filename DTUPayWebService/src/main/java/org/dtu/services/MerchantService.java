package org.dtu.services;


import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import messageUtilities.CorrelationID;
import messageUtilities.queues.IDTUPayMessage;
import messageUtilities.queues.IDTUPayMessageQueue;
import org.dtu.aggregate.Payment;
import org.dtu.aggregate.User;
import org.dtu.events.ConsumeToken;
import org.dtu.events.TokenConsumed;
import org.dtu.exceptions.*;
import org.dtu.factories.CustomerFactory;
import org.dtu.repositories.MerchantRepository;
import org.dtu.repositories.PaymentRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class MerchantService {

    private final BankService bankService;
    private final CustomerService customerService;
    private final Map<CorrelationID, CompletableFuture<IDTUPayMessage>> correlations;
    private final MerchantRepository merchantRepository;
    private final PaymentRepository paymentRepository;
    private final IDTUPayMessageQueue messageQueue;

    public MerchantService(IDTUPayMessageQueue messageQueue, MerchantRepository merchantRepository, PaymentRepository paymentRepository) {
        this.messageQueue = messageQueue;
        this.merchantRepository = merchantRepository;
        this.paymentRepository = paymentRepository;
        this.bankService = new BankServiceService().getBankServicePort();
        this.customerService = new CustomerFactory().getService();
        this.correlations = new ConcurrentHashMap<>();
    }

    public List<User> getMerchants() {
        return merchantRepository.getMerchantList();
    }

    public User getMerchant (UUID id) throws InvalidMerchantIdException {
        try {
             return merchantRepository.getMerchant(id);
        } catch (InvalidMerchantIdException e) {
            throw new InvalidMerchantIdException();
        }
    }

    public Payment createPayment(Payment payment) throws InvalidMerchantIdException, BankServiceException_Exception, InvalidCustomerIdException, CustomerNotFoundException, PaymentAlreadyExistsException, CustomerTokenAlreadyConsumedException {
        ConsumeToken consumeTokenEvent = new ConsumeToken(new CorrelationID(UUID.randomUUID()), payment.getToken());
        correlations.put(consumeTokenEvent.getCorrelationID(), new CompletableFuture<>());
        messageQueue.publish(consumeTokenEvent);

        TokenConsumed consumeTokenEventResult = (TokenConsumed) correlations.get(consumeTokenEvent.getCorrelationID()).join();
        if (!consumeTokenEventResult.getMessage().isEmpty()) {
            throw new CustomerTokenAlreadyConsumedException("Token already consumed");
        }

        User merchant = getMerchant(payment.getMid());
        User customer = customerService.getCustomer(consumeTokenEventResult.getUserId().getUuid());

        try {
            paymentRepository.getPaymentById(payment.getId());
            throw new PaymentAlreadyExistsException("Payment already received");
        } catch (PaymentNotFoundException ignored) { }

        bankService.transferMoneyFromTo(customer.getBankNumber(), merchant.getBankNumber(), BigDecimal.valueOf(payment.getAmount()), "Transfer money");

        paymentRepository.save(payment);
        return payment;
    }

    public User registerMerchant(String firstName, String lastName) throws MerchantAlreadyExistsException {
        try {
            return merchantRepository.addMerchant(firstName, lastName);
        } catch (MerchantAlreadyExistsException e) {
            throw new MerchantAlreadyExistsException();
        }

    }

    public User registerMerchant(String firstName, String lastName, String bankAccount) throws MerchantAlreadyExistsException {
        try {
            return merchantRepository.addMerchant(firstName, lastName, bankAccount);
        } catch (MerchantAlreadyExistsException e) {
            throw new MerchantAlreadyExistsException();
        }
    }

    public ArrayList<User> getMerchantList() {
        return merchantRepository.getMerchantList();
    }

    public User deleteMerchant(UUID id) throws PaymentNotFoundException, MerchantNotFoundException, InvalidMerchantIdException {
        return merchantRepository.deleteMerchant(id);
    }

    private void registerHandlers() {
        this.messageQueue.addHandler(TokenConsumed.class, e -> createPaymentConsumedTokenEventResult((TokenConsumed) e));
    }

    private void createPaymentConsumedTokenEventResult(TokenConsumed event) {
        try {
            correlations.get(event.getCorrelationID()).complete(event);
            correlations.remove(event.getCorrelationID());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
