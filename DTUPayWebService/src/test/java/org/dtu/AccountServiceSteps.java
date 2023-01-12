package org.dtu;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dtu.aggregate.User;
import org.dtu.exceptions.InvalidMerchantIdException;
import org.dtu.exceptions.MerchantAlreadyExistsException;
import org.dtu.factories.MerchantFactory;
import org.dtu.services.MerchantService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountServiceSteps {
    MerchantService merchantService = new MerchantFactory().getService();
    User merchant;
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
}
