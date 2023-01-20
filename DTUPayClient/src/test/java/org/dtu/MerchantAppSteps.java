package org.dtu;

import aggregate.Payment;
import aggregate.Token;
import aggregate.User;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dtu.exceptions.MerchantDoesNotExist;
import org.dtu.exceptions.PaymentDoesNotExist;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.wildfly.common.Assert.assertTrue;

/**
 * @author Sebastian Juste pedersen (s205335)
 * @author Nicklas Olabi (s205347)
 */

public class MerchantAppSteps {

    MerchantApp merchantApp = AppStart.getInstance().getMerchantApp();

    User merchant;

    @When("a merchant is being created")
    public void a_merchant_is_being_created() {
        System.out.println("MerchantServiceSteps");
        try {
            merchant = merchantApp.register("Diana", "Isabel", "MoneyMoneyMoney");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("the merchant registered can be found by the ID")
    public void the_merchant_can_be_found_in_the_system() {
        try {
            assertEquals(merchant, merchantApp.getMerchant(merchant));
        } catch (MerchantDoesNotExist e) {
            throw new RuntimeException(e);
        }
    }

    @Given("a merchant is already in the system")
    public void a_merchant_is_already_in_the_system() {
        try {
            merchant = merchantApp.register("John", "Cena", "IGotMoney");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @When("a merchant is being deleted")
    public void a_merchant_is_being_deleted() {
        try {
            merchantApp.deregister(merchant);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Then("the merchant is no longer in the system")
    public void the_merchant_is_no_longer_in_the_system() {
        try {
            merchantApp.getMerchant(merchant);
            fail();
        } catch (MerchantDoesNotExist e){
            assertTrue(true);
        }
    }

    @Then("the merchant can be found in the system")
    public void theMerchantCanBeFoundInTheSystem() throws MerchantDoesNotExist {
        //assertEquals(merchant.getUserId().getUuid(), merchantApp.getMerchant(merchant).getUserId().getUuid());
        assertEquals(merchant.getUserId().getUuid(), merchantApp.getMerchant(merchant).getUserId().getUuid());
    }

}
