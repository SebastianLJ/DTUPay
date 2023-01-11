package org.dtu;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dtu.*;
import org.dtu.aggregate.Payment;
import org.dtu.aggregate.User;
import org.dtu.factories.PaymentFactory;
import org.dtu.services.PaymentService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentServiceSteps {
    PaymentService paymentRegistration = new PaymentFactory().getService();
    CustomerService customerRegistration = new CustomerService();
    MerchantService merchantRegistration = new MerchantService();

    User customer = null;
    User merchant = null;
    Payment payment = null;
    Payment result = null;

    @Given("^there is a registered customer$")
    public void thereIsARegisteredCustomer() {
        try {
            customer = customerRegistration.addCustomer("Fred", "Again");
        } catch (CustomerAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @And("^there is a registered merchant$")
    public void thereIsARegisteredMerchant() {
        try {
            merchant = merchantRegistration.addMerchant("Some", "Merchant");
        } catch (MerchantAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @When("^the merchant creates a payment$")
    public void theMerchantCreatesAPayment() {
        try {
            payment = paymentRegistration
                    .createPayment(new Payment(customer.getUserId().getUuid(),
                            merchant.getUserId().getUuid(),
                            100));
        } catch (PaymentAlreadyExistsException | InvalidMerchantIdException | InvalidCustomerIdException e) {
            e.printStackTrace();
        }
    }

    @And("^the customer approves it$")
    public void theCustomerApprovesIt() {
    }

    @Then("^a payment is registered$")
    public void aPaymentIsRegistered() {
        try {
            assertNotNull(paymentRegistration.getPayment(payment.getId()));
        } catch (PaymentNotFoundException e) {
            //fail if payment is not found
            fail();
        }
    }

    @Given("there exists a payment")
    public void thereExistsAPayment() {
        this.thereIsARegisteredCustomer();
        this.thereIsARegisteredMerchant();
        this.theMerchantCreatesAPayment();
        this.theCustomerApprovesIt();
        this.aPaymentIsRegistered();
    }

    @When("the id of the payment is queried")
    public void theIdOfThePaymentIsQueried() {
        try {
            result = paymentRegistration.getPayment(payment.getId());
        } catch (PaymentNotFoundException e) {
            result = null;
        }
    }

    @Then("the payment is found")
    public void thePaymentIsFound() {
        assertEquals(payment.getId(), result.getId());
    }

    @Then("the payment is not found")
    public void thePaymentIsNotFound() {
        assertNull(result);
    }

    @Given("the id of a new payment is queried")
    public void theIdOfANewPaymentIsQueried() {
        try {
            result = paymentRegistration.getPayment(UUID.randomUUID());
        } catch (PaymentNotFoundException e) {
            //if payment is not found set result to null
            result = null;
        }
    }
}
