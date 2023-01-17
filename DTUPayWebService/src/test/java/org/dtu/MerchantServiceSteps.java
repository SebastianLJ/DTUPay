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
import org.dtu.aggregate.User;
import org.dtu.exceptions.*;
import org.dtu.factories.MerchantFactory;
import org.dtu.services.MerchantService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

public class MerchantServiceSteps {
    MerchantService merchantService = new MerchantService(new DTUPayRabbitMQ(QueueType.DTUPay, HostnameType.localhost));

    BankService bankService = new BankServiceService().getBankServicePort();

    List<dtu.ws.fastmoney.User> bankUsers = new ArrayList<>();
    dtu.ws.fastmoney.User merchantBankUser;
    User merchant;

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
        merchant = merchantService.registerMerchant(merchantBankUser.getFirstName(), merchantBankUser.getLastName(), bankNumber);
    }

    @When("a merchant is deleted")
    public void a_merchant_is_deleted() throws InvalidMerchantIdException, MerchantNotFoundException, PaymentNotFoundException {
        merchantService.deleteMerchant(merchant.getUserId().getUuid());
    }

    @Then("the merchant cannot be found")
    public void the_merchant_cannot_be_found() throws InvalidMerchantIdException {
        assertNull(merchantService.getMerchant(merchant.getUserId().getUuid()));
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
}
