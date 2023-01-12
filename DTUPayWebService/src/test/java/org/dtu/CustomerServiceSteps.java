package org.dtu;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.User;
import org.dtu.exceptions.CustomerAlreadyExistsException;
import org.dtu.exceptions.InvalidCustomerIdException;
import org.dtu.factories.CustomerFactory;
import org.dtu.services.CustomerService;
import org.junit.After;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerServiceSteps {

    CustomerService service = new CustomerFactory().getService();

    User customer = null;
    ArrayList<Token> tokens;

    public CustomerServiceSteps() {
    }

    // Create customer scenario
    @When("a customer is created")
    public void a_customer_is_created() {
        try {
            customer = service.addCustomer("Frank", "Ocean");
        } catch (CustomerAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @Then("he is registered in the system")
    public void he_is_registered_in_the_system() {
            boolean customerIsInSystem = service.getCustomerList().stream().anyMatch(o -> o.getUserId().equals(customer.getUserId()));
            assertEquals(true, customerIsInSystem);
    }
    @Then("can be found by his ID")
    public void can_be_found_by_his_id() throws InvalidCustomerIdException {
            assertEquals(customer, service.getCustomer(customer.getUserId().getUuid()));
    }

    @After
    public void afterScenario() throws InvalidCustomerIdException {
        service.deleteCustomer(customer.getUserId().getUuid());
    }

    //Delete customer scenario
    @Given("a customer is in the system")
    public void a_customer_is_in_the_system() throws CustomerAlreadyExistsException {
         customer = service.addCustomer("Martin", "Nielsen");
    }
    @When("a customer is deleted")
    public void a_customer_is_deleted() throws InvalidCustomerIdException {
        service.deleteCustomer(customer.getUserId().getUuid());
    }
    @Then("the customer cannot be found")
    public void the_customer_cannot_be_found() throws InvalidCustomerIdException {
        assertNull(service.getCustomer(customer.getUserId().getUuid()));
    }

    @When("the customer requests {int} tokens")
    public void theCustomerRequestsTokens(int tokenCount) {
        tokens = service.getTokens(customer.getUserId(), tokenCount);
    }

    @Then("the customer receives {int} tokens")
    public void theCustomerReceivesTokens(int tokenCount) {
        assertEquals(tokenCount, tokens.size());
    }
}
