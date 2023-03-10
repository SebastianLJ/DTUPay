package org.dtu.services;


import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import messageUtilities.cqrs.CorrelationID;
import messageUtilities.MessageEvent;
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

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public MerchantService(IDTUPayMessageQueue messageQueue, MerchantRepository merchantRepository, PaymentRepository paymentRepository) {
        this.messageQueue = messageQueue;
        this.merchantRepository = merchantRepository;
        this.paymentRepository = paymentRepository;
        this.bankService = new BankServiceService().getBankServicePort();
        this.customerService = new CustomerFactory().getService(messageQueue);
        this.correlations = new ConcurrentHashMap<>();
        registerHandlers();
    }

    public List<User> getMerchants() {
        return merchantRepository.getMerchantList();
    }

    public User getMerchant (UUID id) throws InvalidMerchantIdException {
         return merchantRepository.getMerchant(id);
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public Payment createPayment(Payment payment) throws InvalidMerchantIdException, BankServiceException_Exception, InvalidCustomerIdException, CustomerNotFoundException, PaymentAlreadyExistsException, CustomerTokenAlreadyConsumedException {
        ConsumeToken consumeTokenEvent = new ConsumeToken(new CorrelationID(UUID.randomUUID()), payment.getToken());
        correlations.put(consumeTokenEvent.getCorrelationID(), new CompletableFuture<>());
        MessageEvent newEvent = new MessageEvent("TokenVerificationRequested", new Object[]{consumeTokenEvent});
        messageQueue.publish(newEvent);

        TokenConsumed consumeTokenEventResult = (TokenConsumed) correlations.get(consumeTokenEvent.getCorrelationID()).join();
        if (consumeTokenEventResult.getUserId() == null) {
            throw new CustomerTokenAlreadyConsumedException("Invalid token");
        }

        User merchant = getMerchant(payment.getMid());
        User customer = customerService.getCustomer(consumeTokenEventResult.getUserId().getUuid());

        try {
            paymentRepository.getPaymentById(payment.getId());
            throw new PaymentAlreadyExistsException("Payment already received");
        } catch (PaymentNotFoundException ignored) { }

        bankService.transferMoneyFromTo(customer.getBankNumber(), merchant.getBankNumber(), BigDecimal.valueOf(payment.getAmount()), "Transfer money");
        paymentRepository.save(payment);
        messageQueue.publish(new MessageEvent("MoneyTransferred", new Object[]{payment}));
        return payment;
    }

    /**
     * @author Sebastian Juste pedersen (s205335)
     */
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

    /**
     * @author Sebastian Lund (s184209)
     */
    public User registerMerchant(User user) throws MerchantAlreadyExistsException {
        try {
            return merchantRepository.addMerchant(user);
        } catch (MerchantAlreadyExistsException e) {
            throw new MerchantAlreadyExistsException();
        }
    }

    /**
     * @author Sebastian Juste pedersen (s205335)
     * @author Nicklas Olabi (s205347)
     */

    public ArrayList<User> getMerchantList() {
        return merchantRepository.getMerchantList();
    }

    public User deleteMerchant(UUID id) throws PaymentNotFoundException, MerchantNotFoundException, InvalidMerchantIdException {
        return merchantRepository.deleteMerchant(id);
    }

    public void deletePayment(UUID id) throws PaymentNotFoundException {
        paymentRepository.deletePayment(id);
    }

    private void registerHandlers() {
        this.messageQueue.addHandler("TokenConsumed", e -> {
            TokenConsumed newEvent = e.getArgument(0, TokenConsumed.class);
            createPaymentConsumedTokenEventResult(newEvent);
        });
    }

    /**
     * @author Sebastian Lund (s184209)
     */
    public void createPaymentConsumedTokenEventResult(TokenConsumed event) {
        try {
            correlations.get(event.getCorrelationID()).complete(event);
        } catch (Exception e) {
            throw new Error(e.getMessage());
        }
    }

}
