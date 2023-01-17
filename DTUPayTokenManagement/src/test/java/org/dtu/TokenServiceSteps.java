package org.dtu;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messageUtilities.cqrs.events.Event;
import messageUtilities.queues.QueueType;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import messageUtilities.queues.rabbitmq.HostnameType;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;
import org.dtu.event.TokensGenerated;
import org.dtu.event.TokensRequested;
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


    DTUPayRabbitMQ eventQueue = new DTUPayRabbitMQ(QueueType.DTUPay, HostnameType.localhost);
    TokenService tokenService;



    UserId userId1 = new UserId(UUID.randomUUID());

    Integer amount1;
    UserId userId2 = new UserId(UUID.randomUUID());

    Integer amount2;
    ArrayList<Token> tokens1 = new ArrayList<>();
    ArrayList<Token> tokens2 = new ArrayList<>();

    @When("a message queue is started")
    public void aMessageQueueIsStarted() {
        tokenService = new TokenService(eventQueue);
    }

    @And("a new user is created")
    public void aUserRequestsAnAccount() throws InterruptedException {
        TokensRequested tokensRequested = new TokensRequested(3,userId1);
        eventQueue.publish(tokensRequested);
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
        TokensRequested tokensRequested = new TokensRequested(4,userId2);
        eventQueue.publish(tokensRequested);
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
}
