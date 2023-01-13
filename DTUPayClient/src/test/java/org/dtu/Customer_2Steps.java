package org.dtu;

import aggregate.User;
import aggregate.Token;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Customer_2Steps {
    SimpleDTUPay dtuPay = new SimpleDTUPay();
    CustomerApp customerApp = new CustomerApp();

    User customer;
    List<Token> tokens;

    @And("a customer is in the system")
    public void aCustomerIsInTheSystem() {
        try {
            customer = customerApp.register("Fred", "Again...", "MoneyInTheBank");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @When("the customer requests {int} tokens")
    public void theCustomerRequestsTokens(int amount) {
        try {
            tokens = customerApp.getTokens(customer, amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("the customer receives {int} tokens")
    public void theCustomerReceivesTokens(int amount) {
        assertEquals(amount, tokens.size());
    }


}
