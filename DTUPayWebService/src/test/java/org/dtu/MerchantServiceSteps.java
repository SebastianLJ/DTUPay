package org.dtu;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dtu.aggregate.Payment;
import org.dtu.aggregate.User;
import org.dtu.exceptions.*;
import org.dtu.factories.MerchantFactory;
import org.dtu.services.MerchantService;
import org.junit.After;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MerchantServiceSteps {

    MerchantService merchantRegistration = new MerchantFactory().getService();

    User merchant = null;
    User result = null;

    @When("a merchant is created")
    public void a_merchant_is_created() {
        try {
            merchant = merchantRegistration.addMerchant("John", "Sully");
        } catch (MerchantAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @Then("a merchant is registered in the system")
    public void a_merchant_is_registered_in_the_system() {
        boolean merc = merchantRegistration.getMerchantList().stream().anyMatch(o -> o.getUserId().equals(merchant.getUserId()));
        assertEquals(true, merc);
    }

    @Then("the merchant can be found by his ID.")
    public void the_merchant_can_be_found_by_his_id() throws InvalidMerchantIdException {
        assertEquals(merchant, merchantRegistration.getMerchant(merchant.getUserId().getUuid()));
    }

    @Then("the merchant can't be found")
    public void theMerchantIsNotFound() {
        assertNull(result);
    }

    @After
    public void afterScenario() throws InvalidCustomerIdException, MerchantNotFoundException, PaymentNotFoundException, InvalidMerchantIdException {
        merchantRegistration.deleteMerchant(merchant.getUserId().getUuid());
    }
}

