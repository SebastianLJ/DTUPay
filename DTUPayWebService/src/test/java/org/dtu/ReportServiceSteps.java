package org.dtu;

import dtu.ws.fastmoney.BankServiceException_Exception;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messageUtilities.MessageEvent;
import messageUtilities.cqrs.CorrelationID;
import messageUtilities.queues.IDTUPayMessage;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMq;
import org.dtu.aggregate.Payment;
import org.dtu.aggregate.User;
import org.dtu.aggregate.UserId;
import org.dtu.domain.Token;
import org.dtu.events.UserTokensGenerated;
import org.dtu.events.UserTokensRequested;
import org.dtu.exceptions.*;
import org.dtu.repositories.CustomerRepository;
import org.dtu.repositories.MerchantRepository;
import org.dtu.repositories.PaymentRepository;
import org.dtu.services.CustomerService;
import org.dtu.services.MerchantService;
import org.dtu.services.ReportService;
import org.junit.After;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

public class ReportServiceSteps {

    MerchantService merchantService = new MerchantService(new DTUPayRabbitMq("localhost"), new MerchantRepository(), new PaymentRepository());
    CustomerService customerService = new CustomerService(new DTUPayRabbitMq("localhost"), new CustomerRepository());

    //ConcurrentHashMap<CorrelationID, CompletableFuture<IDTUPayMessage>> publishedEvents = new ConcurrentHashMap<>();

    ConcurrentHashMap<UserId, CompletableFuture<IDTUPayMessage>> publishedUsers = new ConcurrentHashMap<>();


    Payment payment = null;
    List<Payment> merchantPayments = null;
    List<Payment> customerPayments = new ArrayList<>();
    User merchant = null;

    User merchant2 = null;
    User customer = null;

    User customer2 = null;
    Token token = null;

    CompletableFuture<List<Payment>> future = new CompletableFuture<>();

    DTUPayRabbitMq eventQueue = new DTUPayRabbitMq("localhost") {
        @Override
        public void publish(MessageEvent event) {
            switch(event.getType()) {
                case "UserTokensRequested":
                    UserTokensRequested userTokensRequested = event.getArgument(0, UserTokensRequested.class);
                    publishedUsers.get(userTokensRequested.getUserId()).complete(userTokensRequested);
            }
        }
        @Override
        public void addHandler(String eventType, Consumer<MessageEvent> handler){
        }
    };
    ReportService reportService = new ReportService(eventQueue, new PaymentRepository());



    //A merchant retrieves a list of payments
    @Given("a merchant is rregistered in the system")
    public void a_merchant_is_rregistered_in_the_system() throws MerchantAlreadyExistsException {
        merchant = merchantService.registerMerchant("Rob", "Stark", "BankAcc");
    }
    @And("the merchant has been involved in a payment")
    public void the_merchant_has_been_involved_in_a_payment() throws CustomerAlreadyExistsException, CustomerTokenAlreadyConsumedException, BankServiceException_Exception, PaymentAlreadyExistsException, CustomerNotFoundException, InvalidCustomerIdException, InvalidMerchantIdException, PaymentNotFoundException {
       token = new Token();
       payment = new Payment(token, merchant.getUserId().getUuid(), 500);
       reportService.savePayment(payment);
    }
    @When("a merchant retrieves a list of payments")
    public void a_merchant_retrieves_a_list_of_payments() throws PaymentNotFoundException {
        merchantPayments = reportService.getPaymentByMerchantId(merchant.getUserId());
    }
    @Then("the merchant can see a list of all transactions they have been involved in")
    public void the_merchant_can_see_a_list_of_all_transactions_they_have_been_involved_in() {
        assertEquals(payment, merchantPayments.get(0));
    }


    //A merchant cannot retrieve a list of another merchants payments
    @Given("two merchants is registered in the system")
    public void two_merchants_is_registered_in_the_system() throws MerchantAlreadyExistsException {
        merchant = merchantService.registerMerchant("Oberyn", "Martell", "MoneyMoney");
        merchant2 = merchantService.registerMerchant("Arya", "Stark", "BankAcc11");
    }

    @And("merchant1 has been involved in a payment")
    public void merchant1_has_been_involved_in_a_payment() throws CustomerAlreadyExistsException, CustomerTokenAlreadyConsumedException, BankServiceException_Exception, PaymentAlreadyExistsException, CustomerNotFoundException, InvalidCustomerIdException, InvalidMerchantIdException, PaymentNotFoundException {
        token = new Token();
        payment = new Payment(token, merchant.getUserId().getUuid(), 500);
        reportService.savePayment(payment);
    }
    @When("merchant2 retrieves a list of payments")
    public void merchant2_retrieves_a_list_of_payments() throws PaymentNotFoundException {
        merchantPayments = reportService.getPaymentByMerchantId(merchant2.getUserId());
    }
    @Then("the merchant will not be able to see the other merchants payment")
    public void the_merchant_will_not_be_able_to_see_the_other_merchants_payment() {
        assertTrue(merchantPayments.isEmpty());
    }

    //Customer retrieves list of payments
    @Given("a customer is rregistered in the system")
    public void a_customer_is_rregistered_in_the_system() throws CustomerAlreadyExistsException {
        customer = customerService.addCustomer("Tyrion", "Lanister");
        publishedUsers.put(customer.getUserId(), new CompletableFuture<>());
    }

    @And("the customer has been involved in a payment")
    public void the_customer_has_been_involved_in_a_payment() throws CustomerTokenAlreadyConsumedException, InvalidCustomerIdException, BankServiceException_Exception, InvalidMerchantIdException, PaymentAlreadyExistsException, CustomerNotFoundException, MerchantAlreadyExistsException {
       token = new Token();
       payment = new Payment(token, customer.getUserId().getUuid(), 500);
       reportService.savePayment(payment);
    }

    @When("the customer retrieves a list of payments")
    public void the_customer_retrieves_a_list_of_payments() throws PaymentNotFoundException {
        new Thread(()-> {
            try {
                customerPayments = reportService.getPaymentByCustomerId(customer.getUserId());
                future.complete(customerPayments);
            } catch (PaymentNotFoundException e) {
                e.printStackTrace();
            }
        }).start();

    }

    @Then("the customer can see a list of all transactions they have been involved in")
    public void the_customer_can_see_a_list_of_all_transactions_they_have_been_involved_in() {
        publishedUsers.get(customer.getUserId()).join();
        ArrayList<Token> newList = new ArrayList<>();
        CorrelationID correlationID = ((UserTokensRequested) publishedUsers.get(customer.getUserId()).join()).getCorrelationID();
        newList.add(token);
        UserTokensGenerated userTokensGenerated = new UserTokensGenerated(correlationID,customer.getUserId(), newList);
        MessageEvent newEvent = new MessageEvent("UserTokensGenerated", new Object[]{userTokensGenerated});
        reportService.completeEvent(newEvent);

        future.join();

       assertEquals(payment, customerPayments.get(0));
    }

    //A customer cannot retrieve a list of another customers payments
    @Given("two customers is registered in the system")
    public void another_customer_is_registered_in_the_system() throws CustomerAlreadyExistsException, PaymentNotFoundException {
        customer = customerService.addCustomer("Davos", "Seaworth");
        customer2 = customerService.addCustomer("Theon", "Greyjoy");
        publishedUsers.put(customer.getUserId(), new CompletableFuture<>());
        publishedUsers.put(customer2.getUserId(), new CompletableFuture<>());

    }

    @And("customer1 has been involved in a payment")
    public void customer1_has_been_involved_in_a_payment() throws CustomerTokenAlreadyConsumedException, InvalidCustomerIdException, BankServiceException_Exception, InvalidMerchantIdException, PaymentAlreadyExistsException, CustomerNotFoundException, MerchantAlreadyExistsException {
        token = new Token();
        payment = new Payment(token, customer.getUserId().getUuid(), 400);
        reportService.savePayment(payment);
    }

    @When("customer2 retrieves a list of payments")
    public void customer2_retrieves_a_list_of_payments() throws PaymentNotFoundException {
        new Thread(()-> {
            try {
                customerPayments = reportService.getPaymentByCustomerId(customer2.getUserId());
                future.complete(customerPayments);
            } catch (PaymentNotFoundException e) {
                e.printStackTrace();
            }
        }).start();

    }
    @Then("the customer will not be able to see the other customers payment")
    public void the_customer_will_not_be_able_to_see_the_other_customers_payment() {
        publishedUsers.get(customer2.getUserId()).join();
        ArrayList<Token> newList = new ArrayList<>();
        CorrelationID correlationID = ((UserTokensRequested) publishedUsers.get(customer2.getUserId()).join()).getCorrelationID();
        newList.add(token);
        UserTokensGenerated userTokensGenerated = new UserTokensGenerated(correlationID,customer2.getUserId(), newList);
        MessageEvent newEvent = new MessageEvent("UserTokensGenerated", new Object[]{userTokensGenerated});
        reportService.completeEvent(newEvent);

        future.join();

        assertNotEquals(payment, customerPayments.get(0));

    }

    @After
    public void deleteUsersAndPayment() throws CustomerNotFoundException, PaymentNotFoundException, MerchantNotFoundException, InvalidMerchantIdException {
        customerService.deleteCustomer(customer);
        customerService.deleteCustomer(customer2);
        merchantService.deleteMerchant(merchant.getUserId().getUuid());
        merchantService.deleteMerchant(merchant2.getUserId().getUuid());
        merchantService.deletePayment(payment.getId());
    }


}
