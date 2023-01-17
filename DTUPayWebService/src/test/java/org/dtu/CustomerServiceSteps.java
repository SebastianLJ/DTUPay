package org.dtu;

import dtu.ws.fastmoney.AccountInfo;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messageUtilities.CorrelationID;
import messageUtilities.queues.IDTUPayMessage;
import messageUtilities.queues.QueueType;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.User;
import org.dtu.events.AccountDeletionRequested;
import org.dtu.events.CustomerAccountDeleted;
import org.dtu.events.TokensDeleted;
import org.dtu.exceptions.CustomerAlreadyExistsException;
import org.dtu.exceptions.CustomerNotFoundException;
import org.dtu.exceptions.InvalidCustomerIdException;
import org.dtu.exceptions.InvalidCustomerNameException;
import org.dtu.services.CustomerService;
import org.junit.After;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerServiceSteps {


    User customer = null;
    ArrayList<Token> tokens;

    dtu.ws.fastmoney.User customerBankUser;

    BankService bankService = new BankServiceService().getBankServicePort();

    List<dtu.ws.fastmoney.User> bankUsers = new ArrayList<>();

    CompletableFuture<UUID> deletedStudent = new CompletableFuture<>();

    CompletableFuture<IDTUPayMessage> publishedEvents = new CompletableFuture<>();

    private DTUPayRabbitMQ q = new DTUPayRabbitMQ(QueueType.DTUPay) {
        @Override
        public void publish(IDTUPayMessage message) {
            publishedEvents.complete(message);
        }

        @Override
        public void addHandler(Class<? extends IDTUPayMessage> message, Consumer<IDTUPayMessage> handler) {

        }

    };
    CustomerService service = new CustomerService(q);


    public CustomerServiceSteps() {
    }

    // Create customer scenario
    @When("a customer is created")
    public void a_customer_is_created() {
        try {
            customer = service.addCustomer(new User("frank", "ocean", "mybank"));
        } catch (CustomerAlreadyExistsException | InvalidCustomerNameException e) {
            e.printStackTrace();
        }
    }

    @Then("he is registered in the system")
    public void he_is_registered_in_the_system() {
        boolean customerIsInSystem = service.getCustomerList().stream().anyMatch(o -> o.getUserId().equals(customer.getUserId()));
        assertEquals(true, customerIsInSystem);
    }

    @Then("can be found by his ID")
    public void can_be_found_by_his_id() throws InvalidCustomerIdException, CustomerNotFoundException {
        assertEquals(customer, service.getCustomer(customer.getUserId().getUuid()));
    }

    @After
    public void afterScenario() throws InvalidCustomerIdException {
        service.deleteCustomer(customer.getUserId().getUuid());
    }

    //Delete customer scenario
    @Given("a customer is in the system")
    public void a_customer_is_in_the_system() throws CustomerAlreadyExistsException, InvalidCustomerNameException {
        customerBankUser = new dtu.ws.fastmoney.User();
        customerBankUser.setFirstName("Martin");
        customerBankUser.setLastName("Nielsen");
        customerBankUser.setCprNumber("123864-1224");
        String bankNumber;
        try {
            bankNumber = bankService.createAccountWithBalance(customerBankUser, new BigDecimal(1000));
            bankUsers.add(customerBankUser);
        } catch (BankServiceException_Exception e) {
            throw new RuntimeException(e);
        }
        customer = service.addCustomer(new User(customerBankUser.getFirstName(), customerBankUser.getLastName(), bankNumber));
    }

    @When("the customer requests {int} tokens")
    public void theCustomerRequestsTokens(int tokenCount) {
        tokens = service.getTokens(customer.getUserId(), tokenCount);
    }

    @Then("the customer receives {int} tokens")
    public void theCustomerReceivesTokens(int tokenCount) {
        assertEquals(tokenCount, tokens.size());
    }

    @Then("the customer has a bank account")
    public void theCustomerHasABankAccount() {
        try {
            assertNotNull(bankService.getAccount(customer.getBankNumber()));
        } catch (BankServiceException_Exception e) {
            throw new RuntimeException(e);
        }
    }


    @io.cucumber.java.After
    public void deleteBankAccounts() {
        //loop through bankservice.get accounts and delete the ones that are in the list of bank users, using the cpr number to match
        for (dtu.ws.fastmoney.User bankUser :
                bankUsers) {
            //for each accountinfo in getaccounts
            for (AccountInfo accountInfo :
                    bankService.getAccounts()) {
                //if the cpr number of the bank user is equal to the cpr number of the accountinfo
                if (bankUser.getCprNumber().equals(accountInfo.getUser().getCprNumber())) {
                    //delete the account
                    try {
                        bankService.retireAccount(accountInfo.getAccountId());
                    } catch (BankServiceException_Exception e) {
                        fail();
                    }
                }
            }
        }
    }

    @When("the customer is being deleted")
    public void theCustomerIsBeingDeleted() {
        new Thread(() -> {
            try {
                UUID id = service.deleteCustomer(customer.getUserId().getUuid());
                deletedStudent.complete(id);

            } catch (InvalidCustomerIdException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }


    @Then("the AccountDeletionRequested event is sent")
    public void theAccountDeletionRequestedEventIsSent() {
        AccountDeletionRequested event = new AccountDeletionRequested(CorrelationID.randomID(), customer.getUserId().getUuid());
        assertEquals(event, publishedEvents.join());
    }

    @When("the TokensDeleted event is received")
    public void theTokensDeletedEventIsReceived() {
        TokensDeleted event = new TokensDeleted(CorrelationID.randomID(), customer.getUserId().getUuid());
        service.handleCustomerAccountDeleted(event);
    }

    @Then("the customer is deleted")
    public void theCustomerIsDeleted() throws CustomerNotFoundException {
        try {
            deletedStudent.join();
            assertNull(service.getCustomer(customer.getUserId().getUuid()));
        } catch (InvalidCustomerIdException | CustomerNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}

