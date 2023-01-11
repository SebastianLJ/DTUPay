package org.dtu;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.UserId;
import org.dtu.factories.TokenFactory;
import org.dtu.services.TokenService;

import java.util.ArrayList;
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
            tokens1 = tokenService.createUser(userId1);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Then("the generated token list has {int} tokens")
    public void theGeneratedTokenListHasTokens(int arg0) {
        assertEquals(tokens1.size() ,arg0);
    }

    @And("the user is registered")
    public void theUserIsRegistered() {
        assertEquals(tokens1, tokenService.getTokens(userId1));
    }
}
