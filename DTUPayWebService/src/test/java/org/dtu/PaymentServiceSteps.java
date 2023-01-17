package org.dtu;

import dtu.ws.fastmoney.AccountInfo;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messageUtilities.queues.QueueType;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import messageUtilities.queues.rabbitmq.HostnameType;
import org.dtu.aggregate.Payment;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.User;
import org.dtu.factories.CustomerFactory;
import org.dtu.factories.MerchantFactory;
import org.dtu.factories.PaymentFactory;
import org.dtu.services.CustomerService;
import org.dtu.services.MerchantService;
import org.dtu.services.PaymentService;
import org.dtu.exceptions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentServiceSteps {
    PaymentService paymentRegistration = new PaymentFactory().getService();
    CustomerService customerRegistration = new CustomerService(new DTUPayRabbitMQ(QueueType.DTUPay, HostnameType.localhost));
    MerchantService merchantRegistration = new MerchantService(new DTUPayRabbitMQ(QueueType.DTUPay, HostnameType.localhost));

    BankService bankService = new BankServiceService().getBankServicePort();

    dtu.ws.fastmoney.User customerBankUser, merchantBankUser;

    List<dtu.ws.fastmoney.User> bankUsers = new ArrayList<>();
    User customer, merchant = null;
    Payment payment = null;
    Payment result = null;
    List<Payment> payments = new ArrayList<>();

    @Given("^there is a registered customer$")
    public void thereIsARegisteredCustomer() {
        try {

            customer = customerRegistration.addCustomer("Fred", "Again");
        } catch (CustomerAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @And("^there is a registered merchant$")
    public void thereIsARegisteredMerchant() {
        try {
            merchant = merchantRegistration.registerMerchant("Some", "Merchant");
        } catch (MerchantAlreadyExistsException e) {
            e.printStackTrace();
        }
    }


    @Then("^a payment is registered$")
    public void aPaymentIsRegistered() {
        try {
            assertNotNull(paymentRegistration.getPayment(payment.getId()));
        } catch (PaymentNotFoundException e) {
            //fail if payment is not found
            fail();
        }
    }

    @Given("there exists a payment")
    public void thereExistsAPayment() {
        this.thereIsARegisteredCustomer();
        this.thereIsARegisteredMerchant();
        this.aPaymentIsRegistered();
    }

    @When("the id of the payment is queried")
    public void theIdOfThePaymentIsQueried() {
        try {
            result = paymentRegistration.getPayment(payment.getId());
        } catch (PaymentNotFoundException e) {
            result = null;
        }
    }

    @Then("the payment is found")
    public void thePaymentIsFound() {
        assertEquals(payment.getId(), result.getId());
    }

    @Then("the payment is not found")
    public void thePaymentIsNotFound() {
        assertNull(result);
    }

    @Given("the id of a new payment is queried")
    public void theIdOfANewPaymentIsQueried() {
        try {
            result = paymentRegistration.getPayment(UUID.randomUUID());
        } catch (PaymentNotFoundException e) {
            //if payment is not found set result to null
            result = null;
        }
    }

    @When("all payments are queried")
    public void allPaymentsAreQueried() {
        payments = paymentRegistration.getPayments();
    }

    @Then("a list containing {int} payments are returned")
    public void aListContainingPaymentsAreReturned(int count) {
        assertEquals(count, payments.size());
    }

    @After
    public void afterScenario() {
        List<Payment> paymentsToDelete = paymentRegistration.getPayments();
        for (Payment payment:
             paymentsToDelete) {
            try {
                paymentRegistration.deletePayment(payment.getId());
            } catch (PaymentNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Given("a customer with a bank account with balance {int}")
    public void aCustomerWithABankAccountWithBalance(int amount) {
        customerBankUser = new dtu.ws.fastmoney.User();
        customerBankUser.setFirstName("Rob");
        customerBankUser.setLastName("Banks");
        customerBankUser.setCprNumber("123456-2339");
        bankUsers.add(customerBankUser);
        try {
            bankService.createAccountWithBalance(customerBankUser, BigDecimal.valueOf(amount));
        } catch (BankServiceException_Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @And("that the customer is registered with DTU Pay")
    public void thatTheCustomerIsRegisteredWithDTUPay() {
        try {
            customer = customerRegistration.addCustomer(customerBankUser.getFirstName(), customerBankUser.getLastName());
        } catch (CustomerAlreadyExistsException e) {
            fail();
        }
    }

    @Given("a merchant with a bank account with balance {int}")
    public void aMerchantWithABankAccountWithBalance(int amount) {
        merchantBankUser = new dtu.ws.fastmoney.User();
        merchantBankUser.setFirstName("John");
        merchantBankUser.setLastName("Doe");
        merchantBankUser.setCprNumber("123456-2311");
        bankUsers.add(merchantBankUser);
        try {
            bankService.createAccountWithBalance(merchantBankUser, BigDecimal.valueOf(amount));
        } catch (BankServiceException_Exception e) {
            fail();
        }
    }

    @And("that the merchant is registered with DTU Pay")
    public void thatTheMerchantIsRegisteredWithDTUPay() {
        try {
            merchant = merchantRegistration.registerMerchant(merchantBankUser.getFirstName(), merchantBankUser.getLastName());
        } catch (MerchantAlreadyExistsException e) {
            fail();
        }
    }

    @After
    public void deleteBankAccounts(){
        //loop through bankservice.get accounts and delete the ones that are in the list of bank users, using the cpr number to match
        for (dtu.ws.fastmoney.User bankUser:
             bankUsers) {
          //for each accountinfo in getaccounts
            for (AccountInfo accountInfo:
                 bankService.getAccounts()) {
                //if the cpr number of the bank user is equal to the cpr number of the accountinfo
                if(bankUser.getCprNumber().equals(accountInfo.getUser().getCprNumber())){
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

    @When("the merchant initiates a payment for {int} kr by the customer")
    public void theMerchantInitiatesAPaymentForKrByTheCustomer(int amount) {
        Token token = new Token();
        payment = new Payment(token, merchant.getUserId().getUuid(), amount);
        try {
            paymentRegistration.createPayment(payment);
        }  catch (InvalidCustomerIdException | PaymentAlreadyExistsException | InvalidMerchantIdException e) {
            throw new RuntimeException(e);
        }
    }
}
