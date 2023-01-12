package org.dtu;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dtu.aggregate.User;
import org.dtu.exceptions.CustomerAlreadyExistsException;
import org.dtu.exceptions.CustomerNotFoundException;
import org.dtu.exceptions.InvalidCustomerIdException;
import org.dtu.factories.CustomerFactory;
import org.dtu.services.CustomerService;
import org.junit.After;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerServiceSteps {

    CustomerService customerRegistration = new CustomerFactory().getService();

    User customer = null;

    public CustomerServiceSteps() {
    }

    // Create customer scenario
    @When("a customer is created")
    public void a_customer_is_created() {
        try {
            customer = customerRegistration.addCustomer("Frank", "Ocean");
        } catch (CustomerAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @Then("he is registered in the system")
    public void he_is_registered_in_the_system() {
            boolean customerIsInSystem = customerRegistration.getCustomerList().stream().anyMatch(o -> o.getUserId().equals(customer.getUserId()));
            assertEquals(true, customerIsInSystem);
    }
    @Then("can be found by his ID")
    public void can_be_found_by_his_id() throws InvalidCustomerIdException {
        assertEquals(customer, customerRegistration.getCustomer(customer.getUserId().getUuid()));
    }

    @After
    public void afterScenario() throws InvalidCustomerIdException {
        customerRegistration.deleteCustomer(customer.getUserId().getUuid());
    }

    //Delete customer scenario
    @Given("a customer is in the system")
    public void a_customer_is_in_the_system() throws CustomerAlreadyExistsException {
         customer = customerRegistration.addCustomer("Martin", "Nielsen");
    }
    @When("a customer is deleted")
    public void a_customer_is_deleted() throws InvalidCustomerIdException {
        customerRegistration.deleteCustomer(customer.getUserId().getUuid());
    }
    @Then("the customer cannot be found")
    public void the_customer_cannot_be_found() throws InvalidCustomerIdException {
        assertNull(customerRegistration.getCustomer(customer.getUserId().getUuid()));
    }
}
