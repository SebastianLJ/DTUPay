package org.dtu;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;
import messageUtilities.cqrs.events.Event2;
import messageUtilities.queues.QueueType;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ2;
import messageUtilities.queues.rabbitmq.HostnameType;
import org.dtu.domain.Token;
import org.dtu.aggregate.UserId;
import org.dtu.event.*;
import org.dtu.exceptions.*;
import org.dtu.factories.TokenFactory;
import org.dtu.repository.ReadModelRepository;
import org.dtu.repository.TokenRepository;
import org.dtu.services.TokenService;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class TokenServiceSteps {


    DTUPayRabbitMQ2 eventQueue;
    TokenService tokenService;

    UserId userId1 = new UserId(UUID.randomUUID());

    Integer amount1;
    UserId userId2 = new UserId(UUID.randomUUID());

    Integer amount2;
    ArrayList<Token> tokens1 = new ArrayList<>();
    ArrayList<Token> tokens2 = new ArrayList<>();

    ArrayList<Token> generatedTokens = new ArrayList<>();
    ArrayList<Token> usedTokens = new ArrayList<>();
    UUID consumedTokenId;


    private void populateGeneratedTokens(TokensGenerated event){
        generatedTokens.addAll(event.getTokens());
    }

    private void populateUsedTokens(UserTokensGenerated event){
        usedTokens.addAll(event.getTokens());
    }

    @When("a message queue is started")
    public void aMessageQueueIsStarted() {
        eventQueue = new DTUPayRabbitMQ2("localhost");
        tokenService = new TokenService(eventQueue);
        eventQueue.addHandler("TokensGenerated", e -> {
            TokensGenerated newEvent = e.getArgument(0, TokensGenerated.class);
            populateGeneratedTokens(newEvent);
        });
        eventQueue.addHandler("UserTokensGenerated", e -> {
            UserTokensGenerated newEvent = e.getArgument(0, UserTokensGenerated.class);
            populateUsedTokens(newEvent);
        });
    }

    @And("a new user is created")
    public void aUserRequestsAnAccount() throws InterruptedException {
        TokensRequested tokensRequested = new TokensRequested(CorrelationID.randomID(),3,userId1);
        Event2 newEvent = new Event2("TokensRequested", new Object[]{tokensRequested});
        eventQueue.publish(newEvent);
        /*eventQueue.publish(generateToken);*/
        Thread.sleep(1000);
        amount1 = tokenService.getAmountTokensForUser(userId1);
        assertNotNull(amount1);
    }

    @Then("the user has a valid amount of tokens")
    public void theUserHasAValidAmountOfTokens() {
        assertTrue(amount1 > 0);
        assertTrue(amount1 < 7);
    }

    @And("a second user is created")
    public void aSecondUserIsCreated() throws InterruptedException {
        TokensRequested tokensRequested = new TokensRequested(CorrelationID.randomID(),4,userId2);
        Event2 newEvent = new Event2("TokensRequested", new Object[]{tokensRequested});
        eventQueue.publish(newEvent);
        /*eventQueue.publish(generateToken);*/
        Thread.sleep(1000);
        amount2 = tokenService.getAmountTokensForUser(userId2);
        assertNotNull(amount2);
    }

    @Then("they must have different ids")
    public void theyMustHaveDifferentIds() {
        assertNotSame(userId1, userId2);
    }


    @And("they must have different tokens")
    public void theyMustHaveDifferentTokens() {
        assertEquals(7, (int) tokenService.hashmapSize());
    }

    @Then("all tokens are unique")
    public void allTokensAreUnique() {
        Set<UUID> set = new HashSet<>();
        for(Token token : tokens1){
            assertTrue(set.add(token.getId()));
        }
    }

    @And("the new user consumes tokens")
    public void theNewUserConsumesTokens() throws InterruptedException {
        ConsumeToken consumeToken = new ConsumeToken(CorrelationID.randomID(),generatedTokens.get(0));
        consumedTokenId = generatedTokens.get(0).getId();
        Event2 newEvent = new Event2("ConsumeToken", new Object[]{consumeToken});
        eventQueue.publish(newEvent);
        Thread.sleep(1000);
    }


    @Then("he can get a list of the consumed tokens")
    public void heCanGetAListOfTheConsumedTokens() throws InterruptedException {
        UserTokensRequested userTokensRequested = new UserTokensRequested(CorrelationID.randomID(), userId1);
        Event2 newEvent = new Event2("UserTokensRequested", new Object[]{userTokensRequested});
        eventQueue.publish(newEvent);
        Thread.sleep(1000);

        assertEquals(1, usedTokens.size());
        assertEquals(consumedTokenId, usedTokens.get(0).getId());
    }

    @After
    public void afterScenario(){
        generatedTokens.clear();
    }

    @Before
    public void beforeScenario(){
        generatedTokens.clear();
    }
}
