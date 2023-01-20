package org.dtu;

import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messageUtilities.cqrs.CorrelationID;
import messageUtilities.MessageEvent;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ2;
import org.dtu.aggregate.Payment;
import org.dtu.aggregate.User;
import org.dtu.domain.Token;
import org.dtu.events.ConsumeToken;
import org.dtu.events.TokenConsumed;
import org.dtu.exceptions.*;
import org.dtu.repositories.CustomerRepository;
import org.dtu.repositories.MerchantRepository;
import org.dtu.repositories.PaymentRepository;
import org.dtu.services.CustomerService;
import org.dtu.services.MerchantService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Sebastian Lund (s184209)
 * @author Noah Christiansen (s184186)
 */
public class MerchantServiceSteps {

    MerchantRepository repository = new MerchantRepository();
    CustomerRepository customerRepository = new CustomerRepository();
    ConcurrentHashMap<CorrelationID, CompletableFuture<MessageEvent>> eventMap = new ConcurrentHashMap<>();
    DTUPayRabbitMQ2 queue = new DTUPayRabbitMQ2("localhost"){
        @Override
        public void publish(MessageEvent event) {
            if (event.getType().equals("TokenVerificationRequested")) {
                Token token = event.getArgument(0, ConsumeToken.class).getToken();
                tokenEvents.get(token).complete(event);
            } else if (event.getType().equals("MoneyTransferred")) {
                moneyTransferredCompletableFuture.complete(event.getArgument(0, Payment.class));
            } else {
                super.publish(event);

            }
        }
    };
    MerchantService merchantService = new MerchantService(queue, repository, new PaymentRepository());
    CustomerService customerService = new CustomerService(queue, customerRepository);

    ConcurrentHashMap<Token, CompletableFuture<MessageEvent>> tokenEvents = new ConcurrentHashMap<>();

    BankService bankService = new BankServiceService().getBankServicePort();
    String merchantBankNumber;
    String customerBankNumber;
    ArrayList<Token> tokens = new ArrayList<>();

    List<dtu.ws.fastmoney.User> bankUsers = new ArrayList<>();
    dtu.ws.fastmoney.User merchantBankUser;
    User merchant;
    User customer;

    Payment payment;

    CompletableFuture<Payment> paymentTransactionFuture = new CompletableFuture<>();
    CompletableFuture<Payment> moneyTransferredCompletableFuture = new CompletableFuture<>();

    //Create merchant scenario
    @When("a merchant is created")
    public void aMerchantIsCreated() throws MerchantAlreadyExistsException {
        merchant = merchantService.registerMerchant("John", "Doe");
    }

    @Then("a merchant is registered in the system")
    public void theMerchantIsRegisteredInTheSystem() {
        boolean merchantIsInTheSystem = merchantService.getMerchantList().stream().anyMatch(o -> o.getUserId().equals(merchant.getUserId()));
        assertEquals(true, merchantIsInTheSystem);
    }

    @And("the merchant can be found by his ID.")
    public void theMerchantCanBeFoundByHisID() {
        try {
            assertEquals(merchant, merchantService.getMerchant(merchant.getUserId().getUuid()));
        } catch (InvalidMerchantIdException e) {
            throw new RuntimeException(e);
        }
    }

    //Delete merchant scenario
    @Given("a merchant is in the system")
    public void a_merchant_is_in_the_system() throws MerchantAlreadyExistsException {
        merchantBankUser = new dtu.ws.fastmoney.User();
        merchantBankUser.setFirstName("Martin");
        merchantBankUser.setLastName("Nielsen");
        merchantBankUser.setCprNumber("123664-1234");
        String bankNumber;
        try {
            bankNumber = bankService.createAccountWithBalance(merchantBankUser, new BigDecimal(1000));
            bankUsers.add(merchantBankUser);
        } catch (BankServiceException_Exception e) {
            throw new RuntimeException(e);
        }
        merchant = merchantService.registerMerchant(
                merchantBankUser.getFirstName(),
                merchantBankUser.getLastName(),
                bankNumber
        );
    }

    @When("a merchant is deleted")
    public void a_merchant_is_deleted() throws InvalidMerchantIdException, MerchantNotFoundException, PaymentNotFoundException {
        merchantService.deleteMerchant(merchant.getUserId().getUuid());
    }

    @Then("the merchant cannot be found")
    public void the_merchant_cannot_be_found() {
        try {
            merchantService.getMerchant(merchant.getUserId().getUuid());
            fail();
        } catch (InvalidMerchantIdException e) {
            assertTrue(true);
        }
    }

    @Then("the merchant has a bank account")
    public void theMerchantHasABankAccount() {
        try {
            assertNotNull(bankService.getAccount(merchant.getBankNumber()));
        } catch (BankServiceException_Exception e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void deleteBankAccounts() {
        //loop through bankservice.get accounts and delete the ones that are in the list of bank users, using the cpr number to match
        for (dtu.ws.fastmoney.User bankUser :
                bankUsers) {
            //for each accountinfo in getaccounts
            for (AccountInfo accountInfo :
                    bankService.getAccounts()) {
                //if the cpr number of the bank user is equal to the cpr number of the accountinfo
                if (bankUser.getCprNumber().equals(accountInfo.getUser().getCprNumber())) {
                    //delete the account
                    try {
                        bankService.retireAccount(accountInfo.getAccountId());
                    } catch (BankServiceException_Exception e) {
                        fail();
                    }
                }
            }
        }
    }

    @Given("a merchant has a bank account with a balance of {int}")
    public void aMerchantHasABankAccountWithABalanceOf(int balance) throws BankServiceException_Exception {
        dtu.ws.fastmoney.User merchant = new dtu.ws.fastmoney.User();
        merchant.setFirstName("John");
        merchant.setLastName("Doe");
        merchant.setCprNumber(UUID.randomUUID().toString());
        merchantBankNumber =  bankService.createAccountWithBalance(merchant, BigDecimal.valueOf(balance));
    }

    @And("the merchant is a member of DTUPay")
    public void theMerchantIsAMemberOfDTUPay() throws MerchantAlreadyExistsException {
        merchant = merchantService.registerMerchant("John", "Doe", merchantBankNumber);
    }

    @Given("a customer has a bank account with a balance of {int}")
    public void aCustomerHasABankAccountWithABalanceOf(int balance) throws BankServiceException_Exception {
        dtu.ws.fastmoney.User customer = new dtu.ws.fastmoney.User();
        customer.setFirstName("Jane");
        customer.setLastName("Doe");
        customer.setCprNumber(UUID.randomUUID().toString());
        customerBankNumber =  bankService.createAccountWithBalance(customer, BigDecimal.valueOf(balance));
    }

    @And("the customer is a member of DTUPay")
    public void theCustomerIsAMemberOfDTUPay() throws InvalidCustomerNameException, CustomerAlreadyExistsException {
        customer = customerService.addCustomer(
                new User("Jane",
                        "Doe",
                        customerBankNumber)
        );
    }

    @And("the customer has at least one valid token")
    public void theCustomerHasAtLeastOneToken() {
        tokens.add(new Token());
        tokenEvents.put(tokens.get(0), new CompletableFuture<>());
    }

    @And("the customer has one invalid token")
    public void theCustomerHasOneInvalidToken() {
        tokens.add(new Token());
        tokenEvents.put(tokens.get(0), new CompletableFuture<>());
    }

    @When("the merchant initializes a payment of {int}")
    public void theMerchantInitializesAPaymentOf(int amount) {
        payment = new Payment();
        payment.setMid(merchant.getUserId().getUuid());
        payment.setAmount(amount);
    }

    @And("the customer shares a token with the merchant")
    public void theCustomerSharesATokenWithTheMerchant() {
        payment.setToken(tokens.get(0));
    }

    @Then("a payment can be done")
    public void aPaymentCanBeDone() {
        new Thread(() -> {
            try {
                System.out.println("Creating payment");
                merchantService.createPayment(payment);
                System.out.println("Payment created");
            } catch (InvalidMerchantIdException |
                    BankServiceException_Exception |
                    InvalidCustomerIdException |
                    CustomerNotFoundException |
                    PaymentAlreadyExistsException |
                    CustomerTokenAlreadyConsumedException e) {
                fail(e.getMessage());
            }
        }).start();
        ConsumeToken consumeToken = tokenEvents.get(tokens.get(0)).join().getArgument(0, ConsumeToken.class);
        merchantService.createPaymentConsumedTokenEventResult(new TokenConsumed(consumeToken.getCorrelationID(), customer.getUserId()));
        moneyTransferredCompletableFuture.join();
    }

    @Then("the payment fails")
    public void thePaymentFails() {
        new Thread(() -> {
            try {
                merchantService.createPayment(payment);
                fail();
            } catch (CustomerTokenAlreadyConsumedException e) {
                assertEquals("Invalid token", e.getMessage());
            } catch (InvalidCustomerIdException |
                    PaymentAlreadyExistsException |
                    BankServiceException_Exception |
                    CustomerNotFoundException |
                    InvalidMerchantIdException e) {
                fail(e.getMessage());
            }
        }).start();
        ConsumeToken consumeToken = tokenEvents.get(tokens.get(0)).join().getArgument(0, ConsumeToken.class);
        merchantService.createPaymentConsumedTokenEventResult(new TokenConsumed(consumeToken.getCorrelationID(), null));
    }

    private void handleMoneyTransferred(MessageEvent event) {
        moneyTransferredCompletableFuture.complete(event.getArgument(0, Payment.class));
    }

    private void handleTokenVerificationRequestedEvent(MessageEvent event) {
        ConsumeToken requestedEvent = event.getArgument(0, ConsumeToken.class);
        TokenConsumed newEvent = new TokenConsumed(requestedEvent.getCorrelationID(), customer.getUserId());
        queue.publish(new MessageEvent("TokenConsumed", new Object[]{newEvent}));
    }

    private void handleTokenVerificationRequestedEventInvalid(MessageEvent event) {
        ConsumeToken requestedEvent = event.getArgument(0, ConsumeToken.class);
        TokenConsumed newEvent = new TokenConsumed(requestedEvent.getCorrelationID(), null);
        queue.publish(new MessageEvent("TokenConsumed", new Object[]{newEvent}));
    }

    @And("The customer's bank account balance is now {int}")
    public void theCustomerSBankAccountBalanceIsNow(int balance) throws BankServiceException_Exception {
        assertEquals(BigDecimal.valueOf(balance), bankService.getAccount(customerBankNumber).getBalance());
    }

    @And("The merchant's bank account balance is now {int}")
    public void theMerchantSBankAccountBalanceIsNow(int balance) throws BankServiceException_Exception {
        assertEquals(BigDecimal.valueOf(balance), bankService.getAccount(merchantBankNumber).getBalance());
    }

    @After
    public void cleanUp() {
        try {
            bankService.retireAccount(merchantBankNumber);
            bankService.retireAccount(customerBankNumber);
        } catch (BankServiceException_Exception ignored) {
        }
    }
}
