package org.dtu;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messageUtilities.CorrelationID;
import messageUtilities.queues.IDTUPayMessage;
import messageUtilities.queues.QueueType;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import messageUtilities.queues.rabbitmq.HostnameType;
import org.dtu.aggregate.Name;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.User;
import org.dtu.events.CustomerAccountCreated;
import org.dtu.events.TokensGenerated;
import org.dtu.exceptions.CustomerAlreadyExistsException;
import org.dtu.exceptions.InvalidCustomerNameException;
import org.dtu.services.CustomerService;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerServiceSteps {


    User customer;
    Map<Name, CompletableFuture<IDTUPayMessage>> publishedEvents = new HashMap<>();

    CompletableFuture<User> registeredCustomer = new CompletableFuture();

    Map<User, CorrelationID> correlationIDs =new HashMap<>();

    private DTUPayRabbitMQ q = new DTUPayRabbitMQ(QueueType.DTUPay, HostnameType.localhost) {
        @Override
        public void publish(IDTUPayMessage message) {
            if( message instanceof CustomerAccountCreated) {
                CustomerAccountCreated event = (CustomerAccountCreated) message;
                publishedEvents.get(event.getUser().getName()).complete(event);
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
        correlationIDs.put(customer, ((CustomerAccountCreated) event).getCorrelationID());
    }
    @When("the TokensGenerated event is received")
    public void the_tokens_generated_event_is_received() {
        // create list of 5 tokens
        ArrayList<Token> tokens = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tokens.add(new Token());
        }

        service.apply(new TokensGenerated(customer.getUserId(), tokens));
    }
    @Then("the customer is created")
    public void the_customer_is_created() {
        assertNotNull(registeredCustomer.join().getUserId());
    }


    // Create customer scenario

}

