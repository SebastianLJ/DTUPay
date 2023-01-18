package org.dtu;

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
import org.dtu.aggregate.UserId;
import org.dtu.events.AccountDeletionRequested;
import org.dtu.events.CustomerAccountCreated;
import org.dtu.events.TokensDeleted;
import org.dtu.events.TokensGenerated;
import org.dtu.exceptions.CustomerAlreadyExistsException;
import org.dtu.exceptions.CustomerNotFoundException;
import org.dtu.exceptions.InvalidCustomerIdException;
import org.dtu.exceptions.InvalidCustomerNameException;
import org.dtu.repositories.CustomerRepository;
import org.dtu.services.CustomerService;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerServiceSteps {


    User customer;
    Map<Name, CompletableFuture<IDTUPayMessage>> publishedEvents = new HashMap<>();

    CompletableFuture<User> registeredCustomer = new CompletableFuture();

    CompletableFuture<User> deletedCustomer = new CompletableFuture<>();

    CompletableFuture<Boolean> customerNotFound = new CompletableFuture<>();

    Map<User, CorrelationID> correlationIDs = new HashMap<>();

    private DTUPayRabbitMQ q = new DTUPayRabbitMQ(QueueType.DTUPay, HostnameType.localhost) {
        @Override
        public void publish(IDTUPayMessage message) {
            if (message instanceof CustomerAccountCreated) {
                CustomerAccountCreated event = (CustomerAccountCreated) message;
                publishedEvents.get(event.getUser().getName()).complete(event);
            }
            if (message instanceof AccountDeletionRequested) {
                AccountDeletionRequested event = (AccountDeletionRequested) message;
                publishedEvents.get(event.getUser().getName()).complete(event);
            }
        }

        @Override
        public void addHandler(Class<? extends IDTUPayMessage> message, Consumer<IDTUPayMessage> handler) {

        }

    };

    CustomerRepository repository = new CustomerRepository();
    CustomerService service = new CustomerService(q, repository);

    String errorMessage;


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

        service.handleTokensGenerated(new TokensGenerated(customer.getUserId(), tokens));
    }

    @Then("the customer is created")
    public void the_customer_is_created() {
        assertNotNull(registeredCustomer.join().getUserId());
    }

    @Given("a customer is in the system")
    public void aCustomerIsInTheSystem() {
        customer = new User();
        customer.setName(new Name("John", "Doe"));
        publishedEvents.put(customer.getName(), new CompletableFuture<>());
        try {
            customer = repository.addCustomer(customer);
        } catch (CustomerAlreadyExistsException | InvalidCustomerNameException e) {
            throw new RuntimeException(e);
        }
    }

    @When("the customer is being deleted")
    public void theCustomerIsBeingDeleted() {
        new Thread(() -> {
            try {
                User user = service.deleteCustomer(customer);
                deletedCustomer.complete(user);
            } catch (CustomerNotFoundException e) {
                errorMessage = e.getMessage();
                customerNotFound.complete(true);
            }
        }).start();

    }

    @Then("the AccountDeletionRequested event is sent")
    public void theAccountDeletionRequestedEventIsSent() {
        IDTUPayMessage event = publishedEvents.get(customer.getName()).join();
        assertTrue(event instanceof AccountDeletionRequested);
        correlationIDs.put(customer, ((AccountDeletionRequested) event).getCorrelationID());
    }

    @When("the TokensDeleted event is received")
    public void theTokensDeletedEventIsReceived() {
        ArrayList<Token> tokens = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tokens.add(new Token());
        }
        service.handleTokensDeleted(new TokensDeleted(customer));
    }

    @Then("the customer is deleted")
    public void theCustomerIsDeleted() {
        deletedCustomer.join();
        assertEquals(0, repository.getCustomerList().size());
    }

    @Given("a customer is not in the system")
    public void aCustomerIsNotInTheSystem() {
        customer = new User();
        customer.setName(new Name("James", "Bond"));
        customer.setUserId(new UserId(UUID.randomUUID()));
        assertTrue(repository.getCustomerList().isEmpty());
    }

    @Then("the error message {string} is returned")
    public void theErrorMessageIsReturned(String error) {
        customerNotFound.join();
        assertEquals(error, errorMessage);
    }

    // Create customer scenario

}

