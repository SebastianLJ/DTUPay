import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.dtu.*;
import org.dtu.aggregate.Payment;
import org.dtu.aggregate.User;
import org.dtu.factories.PaymentFactory;
import org.dtu.services.PaymentService;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PaymentServiceSteps {
    PaymentService paymentRegistration = new PaymentFactory().getService();
    CustomerService customerRegistration = new CustomerService();
    MerchantService merchantRegistration = new MerchantService();

    User customer = null;
    User merchant = null;
    Payment payment = null;

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
            e.printStackTrace();
        }
    }
}
