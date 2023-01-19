package org.dtu;

import aggregate.Payment;
import aggregate.Token;
import aggregate.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dtu.exceptions.MerchantDoesNotExist;
import org.dtu.exceptions.PaymentDoesNotExist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.wildfly.common.Assert.assertTrue;

public class MerchantAppSteps {

    MerchantApp merchantApp = new MerchantApp();

    User merchant;

    Payment payment;

    @When("a merchant is being created")
    public void a_merchant_is_being_created() {
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
    //Report testing
    @Given("a merchant is rregistered in the system")
    public void a_merchant_is_rregistered_in_the_system() {

    }

    @Given("the merchant has been involved in a payment")
    public void the_merchant_has_been_involved_in_a_payment() throws Exception {

    }

    @When("a merchant retrieves a list of payments")
    public void a_merchant_retrieves_a_list_of_payments() throws PaymentDoesNotExist {

    }

    @Then("the merchant can see a list of all transactions they have been involved in")
    public void the_merchant_can_see_a_list_of_all_transactions_they_have_been_involved_in() throws PaymentDoesNotExist {

    }


    // A merchant cannot retrieve a list of another merchants payments
    @Given("two merchants is registered in the system")
    public void two_merchants_is_registered_in_the_system() {

    }

    @Given("merchant1 has been involved in a payment")
    public void merchant1_has_been_involved_in_a_payment() {

    }

    @When("merchant2 retrieves a list of payments")
    public void merchant2_retrieves_a_list_of_payments() {

    }

    @Then("the merchant will not be able to see the other merchants payment")
    public void the_merchant_will_not_be_able_to_see_the_other_merchants_payment() {

    }


}
