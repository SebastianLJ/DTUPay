package org.dtu;

import aggregate.Payment;
import aggregate.Token;
import aggregate.User;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *@author Sebastian Lund (s184209)
 **/
public class PaymentSteps {
    BankService bankService = new BankServiceService().getBankServicePort();
    CustomerApp customerApp = new CustomerApp();
    MerchantApp merchantApp = new MerchantApp();

    User customer;
    User merchant;

    String customerBankAccount = "";
    String merchantBankAccount = "";

    Payment payment;

    List<Token> customerTokens;

    @Given("a merchant has a bank account with a balance of {int}")
    public void aMerchantHasABankAccountWithABalanceOf(int balance) throws BankServiceException_Exception {
        dtu.ws.fastmoney.User bankUser = new dtu.ws.fastmoney.User();
        bankUser.setFirstName("Fred");
        bankUser.setLastName("Again");
        bankUser.setCprNumber("2309958585");
        merchantBankAccount = bankService.createAccountWithBalance(
                bankUser,
                BigDecimal.valueOf(balance)
        );
    }

    @And("the merchant is a member of DTUPay")
    public void theMerchantIsAMemberOfDTUPay() throws Exception {
        merchant = merchantApp.register("Fred", "Again", merchantBankAccount);
    }

    @Given("a customer has a bank account with a balance of {int}")
    public void aCustomerHasABankAccountWithABalanceOf(int balance) throws BankServiceException_Exception {
        dtu.ws.fastmoney.User bankUser = new dtu.ws.fastmoney.User();
        bankUser.setFirstName("Four");
        bankUser.setLastName("Tet");
        bankUser.setCprNumber("141275293");
        customerBankAccount = bankService.createAccountWithBalance(
                bankUser,
                BigDecimal.valueOf(balance)
        );
    }

    @And("the customer is a member of DTUPay")
    public void theCustomerIsAMemberOfDTUPay() throws Exception {
        customer = customerApp.register("Four", "Tet", customerBankAccount);
    }

    @And("the customer has at least one valid token")
    public void theCustomerHasAtLeastOneValidToken() throws Exception {
        customerTokens = customerApp.generateTokens(customer.getUserId(), 4);
    }

    @And("the customer has an unknown token")
    public void theCustomerHasAnUnknownToken() {
        customerTokens = new ArrayList<>();
        customerTokens.add(new Token());
    }

    @When("the merchant initializes a payment of {int}")
    public void theMerchantInitializesAPaymentOf(int amount) {
        payment = new Payment();
        payment.setAmount(amount);
        payment.setMid(merchant.getUserId().getUuid());
    }

    @And("the customer shares a token with the merchant")
    public void theCustomerSharesATokenWithTheMerchant() {
        payment.setToken(customerTokens.get(0));
    }

    @Then("a payment can be done")
    public void aPaymentCanBeDone() throws Exception {
        merchantApp.pay(merchant.getUserId(), payment.getToken(), payment.getAmount());
    }

    @Then("the payment is rejected with {string}")
    public void thePaymentIsRejected(String message) {
        try {
            merchantApp.pay(merchant.getUserId(), payment.getToken(), payment.getAmount());
            fail("payment succeeded when it should have been rejected");
        } catch (Exception e) {
            assertEquals(message, e.getMessage());
        }
    }

    @Then("The customer's bank account balance is now {int}")
    public void theCustomerSBankAccountBalanceIsNow(int balance) throws BankServiceException_Exception {
        assertEquals(BigDecimal.valueOf(balance),
                bankService.getAccount(customerBankAccount).getBalance());
    }

    @And("The merchant's bank account balance is now {int}")
    public void theMerchantSBankAccountBalanceIsNow(int balance) throws BankServiceException_Exception {
        assertEquals(BigDecimal.valueOf(balance),
                bankService.getAccount(merchantBankAccount).getBalance());

    }

    @After
    public void cleanup(){
        try {
            bankService.retireAccount(customerBankAccount);
        } catch (BankServiceException_Exception ignored) {
        }
        try {
            bankService.retireAccount(merchantBankAccount);
        } catch (BankServiceException_Exception ignored) {
        }
    }
}