package org.dtu;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messageUtilities.CorrelationID;
import messageUtilities.cqrs.events.Event;
import messageUtilities.cqrs.events.Event2;
import messageUtilities.queues.IDTUPayMessage;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ2;
import org.dtu.aggregate.UserId;
import org.dtu.domain.Token;
import org.dtu.event.*;
import org.dtu.services.TokenService;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

public class TokenServiceSteps {

    ConcurrentHashMap<CorrelationID, CompletableFuture<IDTUPayMessage>> publishedEvents = new ConcurrentHashMap<>();

    DTUPayRabbitMQ2 eventQueue = new DTUPayRabbitMQ2("localhost") {
        @Override
        public void publish(Event2 event) {
            switch (event.getType()) {
                case "TokensGenerated":
                    TokensGenerated tokensGenerated = event.getArgument(0, TokensGenerated.class);
                    publishedEvents.get(tokensGenerated.getCorrelationID()).complete(tokensGenerated);
                    break;
                case "UserTokensGenerated":
                    UserTokensGenerated userTokensGenerated = event.getArgument(0, UserTokensGenerated.class);
                    publishedEvents.get(userTokensGenerated.getCorrelationID()).complete(userTokensGenerated);
                    break;
                case "TokenConsumed":
                    TokenConsumed tokenConsumed = event.getArgument(0, TokenConsumed.class);
                    publishedEvents.get(tokenConsumed.getCorrelationID()).complete(tokenConsumed);
                    break;
                case "TokenVerificationRequested":
                    ConsumeToken consumeToken = event.getArgument(0, ConsumeToken.class);
                    tokenService.readModelRepository.apply(consumeToken);
                case "TokensRequested":
                    TokensRequested tokensRequested = event .getArgument(0, TokensRequested.class);
                    tokenService.readModelRepository.apply(tokensRequested);
                case "UserTokensRequested":
                    UserTokensRequested userTokensRequested = event.getArgument(0, UserTokensRequested.class);
                    tokenService.readModelRepository.apply(userTokensRequested);
                default:
                    //super.publish(event);
            }
        }

        @Override
        public void addHandler(String eventType, Consumer<Event2> handler) {
            super.addHandler(eventType, handler);
        }
    };

    ConcurrentHashMap<UserId, ArrayList<Token>> userTokens = new ConcurrentHashMap<>();

    ConcurrentHashMap<UserId, ArrayList<Token>> usedUserTokens = new ConcurrentHashMap<>();

    TokenService tokenService  = new TokenService(eventQueue);

    UserId userId1;
    UserId userId2;

    @When("a message queue is started")
    public void aMessageQueueIsStarted() {

    }

    @And("a new user is created")
    public void aUserRequestsAnAccount() throws InterruptedException {
        userId1 = new UserId(UUID.randomUUID());
        usedUserTokens.put(userId1, new ArrayList<>());
        TokensRequested tokensRequested = new TokensRequested(CorrelationID.randomID(),3,userId1);
        Event2 newEvent = new Event2("TokensRequested", new Object[]{tokensRequested});
        publishedEvents.put(tokensRequested.getCorrelationID(),new CompletableFuture<>());
        eventQueue.publish(newEvent);
        TokensGenerated tokensGenerated = (TokensGenerated) publishedEvents.get(tokensRequested.getCorrelationID()).join();
        userTokens.put(tokensGenerated.getUserid(),tokensGenerated.getTokens());
        assertEquals(userId1, tokensGenerated.getUserid());
        assertNotNull(tokensGenerated.getTokens());
    }

    @Then("the user has a valid amount of tokens")
    public void theUserHasAValidAmountOfTokens() {
        assertTrue(userTokens.get(userId1).size() > 0);
        assertTrue(userTokens.get(userId1).size() < 7);
    }

    @And("a second user is created")
    public void aSecondUserIsCreated() throws InterruptedException {
        userId2 = new UserId(UUID.randomUUID());
        usedUserTokens.put(userId2, new ArrayList<>());
        TokensRequested tokensRequested = new TokensRequested(CorrelationID.randomID(),4,userId2);
        Event2 newEvent = new Event2("TokensRequested", new Object[]{tokensRequested});
        publishedEvents.put(tokensRequested.getCorrelationID(),new CompletableFuture<>());
        eventQueue.publish(newEvent);
        TokensGenerated tokensGenerated = (TokensGenerated) publishedEvents.get(tokensRequested.getCorrelationID()).join();
        userTokens.put(tokensGenerated.getUserid(),tokensGenerated.getTokens());
        assertEquals(userId2, tokensGenerated.getUserid());
        assertNotNull(tokensGenerated.getTokens());
    }

    @Then("they must have different ids")
    public void theyMustHaveDifferentIds() {
        assertNotSame(userId1, userId2);
    }


    @And("they must have different tokens")
    public void theyMustHaveDifferentTokens() {
        assertNotEquals(userTokens.get(userId1),userTokens.get(userId2));
        assertNotEquals(userTokens.get(userId1), userTokens.get(userId2));
        for (Token token : userTokens.get(userId1)){
            assertFalse(userTokens.get(userId2).contains(token));
        }
    }

    @Then("all tokens are unique")
    public void allTokensAreUnique() {
        Set<UUID> set = new HashSet<>();
        for(Token token : userTokens.get(userId1)){
            assertTrue(set.add(token.getId()));
        }
    }

    @And("the new user consumes tokens")
    public void theNewUserConsumesTokens() throws InterruptedException {
        ConsumeToken consumeToken = new ConsumeToken(CorrelationID.randomID(),userTokens.get(userId1).get(0));
        Event2 newEvent = new Event2("TokenVerificationRequested", new Object[]{consumeToken});
        publishedEvents.put(consumeToken.getCorrelationID(),new CompletableFuture<>());
        eventQueue.publish(newEvent);
        TokenConsumed tokenConsumed = (TokenConsumed) publishedEvents.get(consumeToken.getCorrelationID()).join();
        usedUserTokens.get(userId1).add(consumeToken.getToken());
        assertEquals(tokenConsumed.getUserId(),userId1);
        assertEquals(consumeToken.getToken(),userTokens.get(userId1).get(0));
    }


    @Then("he can get a list of the consumed tokens")
    public void heCanGetAListOfTheConsumedTokens() throws InterruptedException {
        UserTokensRequested userTokensRequested = new UserTokensRequested(CorrelationID.randomID(), userId1);
        Event2 newEvent = new Event2("UserTokensRequested", new Object[]{userTokensRequested});
        publishedEvents.put(userTokensRequested.getCorrelationID(),new CompletableFuture<>());
        eventQueue.publish(newEvent);
        UserTokensGenerated userTokensGenerated = (UserTokensGenerated) publishedEvents.get(userTokensRequested.getCorrelationID()).join();
        assertEquals(userId1,userTokensGenerated.getUserId());
        for (Token token : usedUserTokens.get(userId1)){
            assertTrue(userTokensGenerated.getTokens().contains(token));
        }
        assertEquals(usedUserTokens.get(userId1).size(),userTokensGenerated.getTokens().size());
    }

    @Before
    public void resetState(){
        userTokens.clear();
        usedUserTokens.clear();
        publishedEvents.clear();
    }
}
