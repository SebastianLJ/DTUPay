package org.dtu;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;
import org.dtu.exceptions.InvalidTokenAmountException;
import org.dtu.exceptions.TokenAmountExeededException;
import org.dtu.exceptions.UserNotFoundException;
import org.dtu.factories.TokenFactory;
import org.dtu.services.TokenService;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenServiceSteps {

    TokenService tokenService = new TokenFactory().getService();

    UserId userId1 = new UserId(UUID.randomUUID());
    ArrayList<Token> tokens1 = new ArrayList<>();

    @When("a user is created")
    public void aUserRequestsAnAccount() {
        try{
            tokens1 = tokenService.generateTokens(userId1, new Random().nextInt(6) );
        } catch (TokenAmountExeededException | InvalidTokenAmountException e) {
            e.printStackTrace();
        }
    }

    @Then("the token list length is valid")
    public void theTokenListLengthIsValid() {
        assertTrue(tokens1.size() > 0);
        assertTrue(tokens1.size() < 7);
    }

    @And("the user is registered")
    public void theUserIsRegistered() throws UserNotFoundException {
        assertEquals(tokens1, tokenService.getTokens(userId1));
    }

    @When("there exists a user")
    public void thereExistsAUser() {
    }
    @Then("they must have different ids")
    public void theyMustHaveDifferentIds() {
    }
}
