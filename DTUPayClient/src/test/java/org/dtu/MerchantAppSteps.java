package org.dtu;

import aggregate.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dtu.exceptions.MerchantDoesNotExist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.wildfly.common.Assert.assertTrue;

public class MerchantAppSteps {

    MerchantApp merchantApp = new MerchantApp();

    User merchant;

    @When("a merchant is being created")
    public void a_merchant_is_being_created() {
        try {
            merchant = merchantApp.register("Diana", "Isabel", "MoneyMoneyMoney");
            Thread.sleep(1000);
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
