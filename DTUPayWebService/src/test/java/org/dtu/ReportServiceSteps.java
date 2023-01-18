package org.dtu;

import dtu.ws.fastmoney.BankServiceException_Exception;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messageUtilities.queues.IDTUPayMessageQueue;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ2;
import org.dtu.aggregate.Payment;
import org.dtu.aggregate.User;
import org.dtu.domain.Token;
import org.dtu.exceptions.*;
import org.dtu.factories.MerchantFactory;
import org.dtu.repositories.CustomerRepository;
import org.dtu.repositories.MerchantRepository;
import org.dtu.repositories.PaymentRepository;
import org.dtu.services.CustomerService;
import org.dtu.services.MerchantService;
import org.dtu.services.ReportService;
import org.junit.After;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ReportServiceSteps {

    MerchantService merchantService = new MerchantService(new DTUPayRabbitMQ2("localhost"), new MerchantRepository(), new PaymentRepository());
    CustomerService customerService = new CustomerService(new DTUPayRabbitMQ2("localhost"), new CustomerRepository());
    ReportService reportService = new ReportService(new DTUPayRabbitMQ2("localhost"), new PaymentRepository());

    PaymentRepository paymentRepository = new PaymentRepository();

    Payment payment = null;
    List<Payment> merchantPayments = null;
    List<Payment> customerPayments = null;
    User merchant = null;
    User customer = null;

    @Given("a merchant is rregistered in the system")
    public void a_merchant_is_rregistered_in_the_system() throws MerchantAlreadyExistsException {
        merchant = merchantService.registerMerchant("Rob", "Stark", "BankAcc");
    }
    @Given("the merchant has been involved in a payment")
    public void the_merchant_has_been_involved_in_a_payment() throws CustomerAlreadyExistsException, CustomerTokenAlreadyConsumedException, BankServiceException_Exception, PaymentAlreadyExistsException, CustomerNotFoundException, InvalidCustomerIdException, InvalidMerchantIdException {
        customer = customerService.addCustomer("John", "Snow");
        List<Token> tokens = customerService.getTokens(customer.getUserId(), 1);
        Token token = tokens.get(0);
        payment = Payment.create(token, merchant.getUserId().getUuid(), 500);
        merchantService.createPayment(payment);
    }
    @When("a merchant retrieves a list of payments")
    public void a_merchant_retrieves_a_list_of_payments() throws PaymentNotFoundException {
        merchantPayments = reportService.getPaymentByMerchantId(merchant.getUserId());
    }
    @Then("the merchant can see a list of all transactions they have been involved in")
    public void the_merchant_can_see_a_list_of_all_transactions_they_have_been_involved_in() {
        assertEquals(payment, merchantPayments.get(0));
    }

    @Given("a customer is registered in the system")
    public void a_customer_is_registered_in_the_system() throws CustomerAlreadyExistsException {
        customer = customerService.addCustomer("Tyrion", "Lanister");
    }
    @Given("the customer has been involved in a payment")
    public void the_customer_has_been_involved_in_a_payment() throws CustomerTokenAlreadyConsumedException, InvalidCustomerIdException, BankServiceException_Exception, InvalidMerchantIdException, PaymentAlreadyExistsException, CustomerNotFoundException, MerchantAlreadyExistsException {
        merchant = merchantService.registerMerchant("Cerci", "Lanister", "theIronBank");
        List<Token> tokens = customerService.getTokens(customer.getUserId(), 1);
        Token token = tokens.get(0);
        payment = Payment.create(token, merchant.getUserId().getUuid(), 500);
        merchantService.createPayment(payment);
    }

    @When("the customer retrieves a list of payments")
    public void the_customer_retrieves_a_list_of_payments() {
        //customerPayments = reportService.getPaymentByCustomerId(customer.getUserId());

    }
    @Then("the customer can see a list of all tra")
    public void the_customer_can_see_a_list_of_all_tra() {
        //assertEquals(payment, customerPayments.get(0));
    }

    @After
    public void delete() {

    }

}
