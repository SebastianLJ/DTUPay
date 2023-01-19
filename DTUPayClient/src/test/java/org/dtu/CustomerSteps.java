package org.dtu;

import aggregate.User;
import aggregate.Token;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dtu.exceptions.CustomerDoesNotExist;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerSteps {
    CustomerApp customerApp = new CustomerApp();

    User customer;
    List<Token> tokens;

    @And("a customer is in the system")
    public void aCustomerIsInTheSystem() {
        try {
            customer = customerApp.register("Fred", "Again...", "MoneyInTheBank");
            //customer = dtuPay.createDTUPayCustomerAccount("fred", "again");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @When("the customer requests {int} tokens")
    public void theCustomerRequestsTokens(int amount) {
        try {
            tokens = customerApp.generateTokens(customer.getUserId(), amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("the customer receives {int} tokens")
    public void theCustomerReceivesTokens(int amount) {
        assertEquals(amount, tokens.size());
    }


    //Test of customer deleted along with its tokens
    @Given("a customer is already in the system")
    public void a_customer_is_already_in_the_system() {
        try {
            customer = customerApp.register("Jake", "Sully", "BankAccount112233");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @When("a customer is deleted")
    public void a_customer_is_deleted() {
        try {
            customerApp.deRegisterCustomer(customer);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Then("the customer is no longer in the system")
    public void the_customer_is_no_longer_in_the_system() {
        try {
            customerApp.getCustomer(customer);
            fail();
        } catch (CustomerDoesNotExist e) {
            assertTrue(true);

        }
    }

    @Then("the customers tokens have been deleted")
    public void the_customers_tokens_have_been_deleted() {
        // todo
        // Write code here that turns the phrase above into concrete actions
        //throw new io.cucumber.java.PendingException();
    }

    //Report testing
    @Given("a customer is rregistered in the system")
    public void a_customer_is_rregistered_in_the_system() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Given("the customer has been involved in a payment")
    public void the_customer_has_been_involved_in_a_payment() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("the customer retrieves a list of payments")
    public void the_customer_retrieves_a_list_of_payments() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("the customer can see a list of all transactions they have been involved in")
    public void the_customer_can_see_a_list_of_all_transactions_they_have_been_involved_in() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    // A customer cannot retrieve a list of another customers payments
    @Given("two customers is registered in the system")
    public void two_customers_is_registered_in_the_system() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Given("customer1 has been involved in a payment")
    public void customer1_has_been_involved_in_a_payment() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("customer2 retrieves a list of payments")
    public void customer2_retrieves_a_list_of_payments() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("the customer will not be able to see the other customers payment")
    public void the_customer_will_not_be_able_to_see_the_other_customers_payment() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }






}
