package org.dtu;

import aggregate.User;
import aggregate.UserId;
import aggregate.Token;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerSteps {
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
            tokens = customerApp.getTokens(customer.getUserId(), amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("the customer receives {int} tokens")
    public void theCustomerReceivesTokens(int amount) {
        assertEquals(amount, tokens.size());
    }


    @Given("a customer is already in the system")
    public void a_customer_is_already_in_the_system() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @When("a customer is deleted")
    public void a_customer_is_deleted() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Then("the customer is no longer in the system")
    public void the_customer_is_no_longer_in_the_system() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Then("the customers tokens have been deleted")
    public void the_customers_tokens_have_been_deleted() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }



}
