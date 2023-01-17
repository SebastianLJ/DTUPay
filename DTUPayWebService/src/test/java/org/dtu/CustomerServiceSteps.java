package org.dtu;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messageUtilities.queues.IDTUPayMessage;
import messageUtilities.queues.QueueType;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import org.dtu.aggregate.Name;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.User;
import org.dtu.events.CustomerAccountCreated;
import org.dtu.exceptions.CustomerAlreadyExistsException;
import org.dtu.exceptions.InvalidCustomerNameException;
import org.dtu.services.CustomerService;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerServiceSteps {


    User customer;
    Map<Name, CompletableFuture<IDTUPayMessage>> publishedEvents = new HashMap<>();

    CompletableFuture<User> registeredCustomer = new CompletableFuture();

    private DTUPayRabbitMQ q = new DTUPayRabbitMQ(QueueType.DTUPay) {
        @Override
        public void publish(IDTUPayMessage message) {
            System.out.println("killmyself");
            if( message instanceof CustomerAccountCreated) {
                System.out.println("CustomerAccountCreated");
                CustomerAccountCreated event = (CustomerAccountCreated) message;
                publishedEvents.get(event.getUser().getName()).complete(event);
                System.out.println("test");
            }
        }

        @Override
        public void addHandler(Class<? extends IDTUPayMessage> message, Consumer<IDTUPayMessage> handler) {

        }

    };
    CustomerService service = new CustomerService(q);


    public CustomerServiceSteps() {
    }


    @Given("there is a customer with an empty id")
    public void thereIsACustomerWithAnEmptyId() {
        System.out.println("hello");
        customer = new User();
        customer.setName(new Name("James", "Bond"));
        publishedEvents.put(customer.getName(), new CompletableFuture<>());
        assertNull(customer.getUserId());
    }

    @When("a customer is being created")
    public void a_customer_is_being_created() {
        new Thread(() -> {
            try {
                User user = service.addCustomer(customer);
                registeredCustomer.complete(user);
            } catch (CustomerAlreadyExistsException | InvalidCustomerNameException e) {
                throw new RuntimeException(e);
            }
        }).start();
        // Write code here that turns the phrase above into concrete actions
    }
    @Then("the CustomerAccountCreated event is sent")
    public void the_customer_account_created_event_is_sent() {
        // Write code here that turns the phrase above into concrete actions
        IDTUPayMessage event = publishedEvents.get(customer.getName()).join();
        assertTrue(event instanceof CustomerAccountCreated);
    }
    @When("the TokensGenerated event is received")
    public void the_tokens_generated_event_is_received() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Then("the customer is created")
    public void the_customer_is_created() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Then("the customer has tokens")
    public void the_customer_has_tokens() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    // Create customer scenario

}

