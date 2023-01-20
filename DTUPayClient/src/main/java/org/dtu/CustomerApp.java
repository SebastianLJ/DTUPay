package org.dtu;

import aggregate.Payment;
import aggregate.Token;
import aggregate.User;
import aggregate.UserId;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.dtu.exceptions.CustomerDoesNotExist;
import org.dtu.exceptions.PaymentDoesNotExist;

import java.util.List;

public class CustomerApp {
    private final Client client = ClientBuilder.newClient();
    private final WebTarget webTarget = client.target("http://localhost:8080/");

    /**
     * @author Sebastian Lund (s184209)
     */
    public User register(String firstName, String lastName, String bankNumber) throws Exception {
        User user = new User(firstName, lastName, bankNumber);
        try (Response response = webTarget.path("customers")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(user, MediaType.APPLICATION_JSON))) {
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(User.class);
            } else {
                System.out.println("!!!! " + response.readEntity(String.class) + " !!!!");
                throw new Exception("code: " + response.getStatus());
            }
        }
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public User deRegisterCustomer(User user) throws Exception {
        try (Response response = webTarget.path("customers/" + user.getUserId().getUuid())
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .delete()) {
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(User.class);
            } else {
                throw new Exception("code: " + response.getStatus());
            }
        }
    }

    /**
     * @author Sebastian Lund (s184209)
     */
    public List<Token> generateTokens(UserId userId, int tokenCount) throws Exception {
        try (Response response = webTarget.path("customers/" + userId.getUuid() + "/tokens")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .put(Entity.entity(tokenCount, MediaType.APPLICATION_JSON))) {
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(new GenericType<List<Token>>() {
                });
            } else {
                throw new Exception("code: " + response.getStatus() + "\n" + response.readEntity(String.class));
            }
        }
    }

    /**
     * @author Sebastian Lund (s184209)
     */
    public User getCustomer(User user) throws CustomerDoesNotExist {
        try (Response response = webTarget.path("customers/" + user.getUserId().getUuid())
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get()) {
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(User.class);
            } else {
                throw new CustomerDoesNotExist();
            }
        }
    }

}
