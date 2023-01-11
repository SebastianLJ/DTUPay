package org.dtu;

import dtu.ws.fastmoney.AccountInfo;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.User;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.After;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DTUPaySteps {
    String cid,mid;
    SimpleDTUPay dtuPay = new SimpleDTUPay();
    boolean successful;

    List<Payment> payments;
    String errorMessage;

    List<AccountInfo> accounts = new ArrayList<>();

    User customerBankAccount = new User();
    User merchantBankAccount = new User();
    List<User> users = new ArrayList<>();

    aggregate.User customerDTUPayAccount;
    aggregate.User merchantDTUPayAccount;


    @When("the merchant initiates a payment for {int} kr by the customer")
    public void theMerchantInitiatesAPaymentForKrByTheCustomer(int amount) {
        try {
            successful = dtuPay.pay(customerDTUPayAccount.getUserId().getUuid(), merchantDTUPayAccount.getUserId().getUuid(), amount);
        } catch (CustomerDoesNotExist | MerchantDoesNotExist | BankServiceException_Exception | PaymentAlreadyExists e) {
            successful = false;
            errorMessage = e.getMessage();
        }
    }

    @Then("the payment is successful")
    public void thePaymentIsSuccessful() {
        assertTrue(successful);
    }

    @When("the manager asks for a list of payments")
    public void theManagerAsksForAListOfPayments() {
        payments = dtuPay.getPayments();
    }



    @Then("the payment is not successful")
    public void thePaymentIsNotSuccessful() {
        assertFalse(successful);
    }

    @And("an error message is returned saying {string}")
    public void anErrorMessageIsReturnedSaying(String errorString) {
        assertTrue(errorMessage.contains(errorString));
    }

    @Given("a customer with a bank account with balance {int}")
    public void aCustomerWithABankAccountWithBalance(int amount) {
        //find the customer with amount
        customerBankAccount.setFirstName("John");
        customerBankAccount.setLastName("DeezNuts");
        customerBankAccount.setCprNumber("123456-1323");
        users.add(customerBankAccount);
        try {
            cid = dtuPay.createBankAccountWithBalance(customerBankAccount, BigDecimal.valueOf(amount));
        } catch (BankServiceException_Exception e) {
            throw new RuntimeException(e);
        }
    }

    @And("that the customer is registered with DTU Pay")
    public void thatTheCustomerIsRegisteredWithDTUPay() {
        customerDTUPayAccount = dtuPay.createDTUPayCustomerAccount(customerBankAccount.getFirstName(), customerBankAccount.getLastName());
        assertNotNull(customerDTUPayAccount.getUserId().getUuid());
    }

    @Given("a merchant with a bank account with balance {int}")
    public void aMerchantWithABankAccountWithBalance(int amount) {
        merchantBankAccount.setFirstName("Bob");
        merchantBankAccount.setLastName("Doe");
        merchantBankAccount.setCprNumber("123456-9982");
        users.add(merchantBankAccount);
        try {
            mid = dtuPay.createBankAccountWithBalance(merchantBankAccount, BigDecimal.valueOf(amount));
        } catch (BankServiceException_Exception e) {
            throw new RuntimeException(e);
        }
    }

    @And("that the merchant is registered with DTU Pay")
    public void thatTheMerchantIsRegisteredWithDTUPay() {
        merchantDTUPayAccount = dtuPay.createDTUPayMerchantAccount(merchantBankAccount.getFirstName(), merchantBankAccount.getLastName());
        assertNotNull(merchantDTUPayAccount.getUserId().getUuid());
    }

    @And("the balance of the customer at the bank is {int} kr")
    public void theBalanceOfTheCustomerAtTheBankIsKr(int amount) {
        try {
            if(dtuPay.getBalance(cid) != amount) successful = false;
        } catch (BankServiceException_Exception e) {
            successful = false;
        }
    }

    @And("the balance of the merchant at the bank is {int} kr")
    public void theBalanceOfTheMerchantAtTheBankIsKr(int amount) {
        try {
            if(dtuPay.getBalance(mid) != amount) successful = false;
        } catch (BankServiceException_Exception e) {
            successful = false;
        }
    }

    @After
    public void tearDown() {
        //for each user in the users list retire their account id
        accounts = dtuPay.getBankAccounts();
        for (User user : users) {
            for (AccountInfo account : accounts) {
                if (account.getUser().getCprNumber().equals(user.getCprNumber())) {
                    try {
                        dtuPay.retireBankAccount(account.getAccountId());
                    } catch (BankServiceException_Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    }

    @Then("the list contains a payment where the customer paid {string} kr to the merchant")
    public void theListContainsAPaymentWhereTheCustomerPaidKrToTheMerchant(String amount) {
        boolean found = false;
        for (Payment p : payments) {
            if (p.getCid().equals(cid) && p.getMid().equals(mid) && p.getAmount() == Integer.parseInt(amount)) {
                found = true;
            }
        }
        assertTrue(found);
    }
}
