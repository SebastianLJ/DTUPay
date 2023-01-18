package org.dtu;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dtu.messageUtilities.cqrs.events.Event;
import org.dtu.messageUtilities.queues.QueueType;
import org.dtu.messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import org.dtu.messageUtilities.queues.rabbitmq.HostnameType;
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


    DTUPayRabbitMQ eventQueue = new DTUPayRabbitMQ(QueueType.DTUPay, HostnameType.localhost);
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
        tokenService = new TokenService(eventQueue);
        eventQueue.addHandler(TokensGenerated.class, e -> populateGeneratedTokens((TokensGenerated) e));
        eventQueue.addHandler(UserTokensGenerated.class, e -> populateUsedTokens((UserTokensGenerated) e));
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

    @And("the new user consumes tokens")
    public void theNewUserConsumesTokens() throws InterruptedException {
        ConsumeToken consumeToken = new ConsumeToken(generatedTokens.get(0));
        consumedTokenId = generatedTokens.get(0).getId();
        eventQueue.publish(consumeToken);
        Thread.sleep(1000);
    }


    @Then("he can get a list of the consumed tokens")
    public void heCanGetAListOfTheConsumedTokens() throws InterruptedException {
        UserTokensRequested userTokensRequested = new UserTokensRequested(userId1);
        eventQueue.publish(userTokensRequested);
        Thread.sleep(1000);

        assertEquals(1, usedTokens.size());
        assertEquals(consumedTokenId, usedTokens.get(0).getId());
    }
}
