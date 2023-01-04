import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.CustomerDoesNotExist;
import org.example.MerchantDoesNotExist;
import org.example.Payment;
import org.example.SimpleDTUPay;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DTUPaySteps {
    String cid,mid;
    SimpleDTUPay dtuPay = new SimpleDTUPay();
    boolean successful;

    List<Payment> payments;
    String errorMessage;
    @Given("a customer with id {string}")
    public void aCustomerWithId(String cid) {
        this.cid = cid;
    }

    @And("a merchant with id {string}")
    public void aMerchantWithId(String mid) {
        this.mid = mid;
    }

    @When("the merchant initiates a payment for {int} kr by the customer")
    public void theMerchantInitiatesAPaymentForKrByTheCustomer(int amount) {
        try {
            successful = dtuPay.pay(cid, mid, amount);
        } catch (CustomerDoesNotExist | MerchantDoesNotExist e) {
            successful = false;
            errorMessage = e.getMessage();
        }
    }

    @Then("the payment is successful")
    public void thePaymentIsSuccessful() {
        assertTrue(successful);
    }

    @Given("a successful payment of {string} kr from customer {string} to merchant {string}")
    public void aSuccessfulPaymentOfKrFromCustomerToMerchant(String amount, String cid, String mid) {
        try {
            successful = dtuPay.pay(cid,mid,Integer.parseInt(amount));
        } catch (CustomerDoesNotExist | MerchantDoesNotExist e) {
            successful = false;
            errorMessage = e.getMessage();
        }
        assertTrue(successful);
    }

    @When("the manager asks for a list of payments")
    public void theManagerAsksForAListOfPayments() {
        payments = dtuPay.getPayments();
    }

    @Then("the list contains a payment where customer {string} paid {string} kr to merchant {string}")
    public void theListContainsAPaymentWhereCustomerPaidKrToMerchant(String cid, String amount, String mid) {
        boolean found = false;
        for (Payment p : payments) {
            if (p.getCid().equals(cid) && p.getMid().equals(mid) && p.getAmount() == Integer.parseInt(amount)) {
                found = true;
            }
        }
        assertTrue(found);

    }

    @When("the merchant initiates a payment for {string} kr by the customer")
    public void theMerchantInitiatesAPaymentForKrByTheCustomer(String amount) {
        try {
            successful = dtuPay.pay(cid, mid, Integer.parseInt(amount));
        } catch (CustomerDoesNotExist | MerchantDoesNotExist e) {
            successful = false;
            errorMessage = e.getMessage();
            System.out.println("insteps" + errorMessage);
        }
    }

    @Then("the payment is not successful")
    public void thePaymentIsNotSuccessful() {
        assertFalse(successful);
    }

    @And("an error message is returned saying {string}")
    public void anErrorMessageIsReturnedSaying(String errorString) {
        System.out.println(errorMessage);
        assertTrue(errorMessage.contains(errorString));
    }
}
