package org.dtu;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messageUtilities.queues.QueueType;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;
import org.dtu.exceptions.*;
import org.dtu.factories.TokenFactory;
import org.dtu.services.TokenService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TokenServiceSteps {

    TokenService tokenService = new TokenFactory(new DTUPayRabbitMQ(QueueType.DTUPay_TokenManagement)).getService();

    UserId userId1 = new UserId(UUID.randomUUID());
    UserId userId2 = new UserId(UUID.randomUUID());
    ArrayList<Token> tokens1 = new ArrayList<>();
    ArrayList<Token> tokens2 = new ArrayList<>();

    @When("a user is created")
    public void aUserRequestsAnAccount() throws InvalidTokenAmountException, InvalidTokenAmountRequestException {
        tokens1 = tokenService.generateTokens(userId1, new Random().nextInt(5 - 1) + 1);
    }

    @Then("the token list length is valid")
    public void theTokenListLengthIsValid() {
        assertTrue(tokens1.size() > 0);
        assertTrue(tokens1.size() < 7);
    }

    @And("the user is registered")
    public void theUserIsRegistered() throws TokenHasAlreadyBeenUsedException, TokenDoesNotExistException, NoMoreValidTokensException {
        for (Token token : tokens1) {
            UserId user = tokenService.consumeToken(token);
            assertNotNull(user);
        }
    }

    @And("a second is created")
    public void aSecondIsCreated() throws InvalidTokenAmountException, InvalidTokenAmountRequestException {
        tokens2 = tokenService.generateTokens(userId2, new Random().nextInt(5));
    }

    @Then("they must have different ids")
    public void theyMustHaveDifferentIds() {
        assertNotSame(userId1, userId2);
    }


    @And("they must have different tokens")
    public void theyMustHaveDifferentTokens() {
        ArrayList<Token> common = new ArrayList<>(tokens1);
        common.retainAll(tokens2);
        assertEquals(0, common.size());
    }

    @Then("all tokens are unique")
    public void allTokensAreUnique() {
        Set<UUID> set = new HashSet<>();
        for(Token token : tokens1){
            assertTrue(set.add(token.getId()));
        }
    }
}
