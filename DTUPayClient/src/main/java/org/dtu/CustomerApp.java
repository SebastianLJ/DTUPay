package org.dtu;

import aggregate.Name;
import aggregate.Token;
import aggregate.User;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerApp {
    Client c = ClientBuilder.newClient();
    WebTarget r = c.target("http://localhost:8080/");

    public UUID register(String firstName, String lastName, String bankAccount) throws Exception {
        User user = new User(firstName, lastName, bankAccount);
        Response response = r.path("customers")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(user, MediaType.APPLICATION_JSON));
        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            return response.readEntity(UUID.class);
        } else {
            throw new Exception("code: " + response.getStatus());
        }
    }

    public List<Token> getTokens(UUID user, int tokenCount) throws Exception {
        Response response = r.path(
                        "customers/"+
                        user+
                        "/tokens"
                ).request()
                .accept(MediaType.APPLICATION_JSON)
                .put(Entity.entity(tokenCount, MediaType.APPLICATION_JSON));
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<List<Token>>() {
            });
        } else {
            throw new Exception("code: " + response.getStatus());
        }
    }
}
