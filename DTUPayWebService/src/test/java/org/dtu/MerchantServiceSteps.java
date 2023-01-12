package org.dtu;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dtu.aggregate.User;
import org.dtu.exceptions.*;
import org.dtu.factories.MerchantFactory;
import org.dtu.services.MerchantService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MerchantServiceSteps {
    MerchantService merchantService = new MerchantFactory().getService();
    User merchant;

    //Create merchant scenario
    @When("a merchant is created")
    public void aMerchantIsCreated() throws MerchantAlreadyExistsException {
        merchant = merchantService.addMerchant("John", "Doe");
    }

    @Then("a merchant is registered in the system")
    public void aMerchantIsRegisteredInTheSystem() {

        //search for merchant in getMerchants
        assertEquals(merchant, merchantService.getMerchants().get(0));
    }

    @And("the merchant can be found by his ID.")
    public void theMerchantCanBeFoundByHisID() {
        try {
            assertEquals(merchant,merchantService.getMerchant(merchant.getUserId().getUuid()));
        } catch (InvalidMerchantIdException e) {
            throw new RuntimeException(e);
        }
    }

    //Delete merchant scenario
    @Given("a merchant is in the system")
    public void a_merchant_is_in_the_system() throws MerchantAlreadyExistsException {
        merchant = merchantService.addMerchant("Martin", "Nielsen");
    }
    @When("a merchant is deleted")
    public void a_merchant_is_deleted() throws InvalidMerchantIdException, MerchantNotFoundException, PaymentNotFoundException {
        merchantService.deleteMerchant(merchant.getUserId().getUuid());
    }
    @Then("the merchant cannot be found")
    public void the_merchant_cannot_be_found() throws InvalidMerchantIdException {
        assertNull(merchantService.getMerchant(merchant.getUserId().getUuid()));
    }
}
