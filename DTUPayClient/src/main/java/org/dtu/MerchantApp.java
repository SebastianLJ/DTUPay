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
import org.dtu.exceptions.MerchantDoesNotExist;
import org.dtu.exceptions.PaymentDoesNotExist;

import java.util.List;

public class MerchantApp {
    private final Client client = ClientBuilder.newClient();
    private final WebTarget webTarget = client.target("http://localhost:8080/");

    /**
     * @author Sebastian Lund (s184209)
     */
    public User register(String firstName, String lastName, String bankNumber) throws Exception {
        User user = new User(firstName, lastName, bankNumber);
        try (Response response = webTarget.path("merchants")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(user, MediaType.APPLICATION_JSON))) {
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(User.class);
            } else {
                System.out.println(response.getStatus() +" "+ Response.Status.OK.getStatusCode());
                System.out.println("!!!! " + response.readEntity(String.class) + " !!!!");
                throw new Exception("code: " + response.getStatus());
            }
        }
    }
    /**
     * @author Nicklas Olabi (s205347)
     */
    public User deregister(User user) throws Exception {
        try (Response response = webTarget.path("merchants/" + user.getUserId().getUuid())
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
    public Payment pay(UserId merchantId, Token customerToken, int amount) throws Exception {
        Payment payment = new Payment(customerToken, merchantId.getUuid(), amount);
        try (Response response = webTarget.path("merchants/payments")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(payment, MediaType.APPLICATION_JSON))) {
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                return response.readEntity(Payment.class);
            } else {
                throw new Exception("code: " + response.getStatus() + " message: " + response.readEntity(String.class));
            }
        }
    }

    /**
     * @author Sebastian Juste pedersen (s205335)
     */
    public User getMerchant(User user) throws MerchantDoesNotExist {
        try (Response response = webTarget.path("merchants/" + user.getUserId().getUuid())
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get()) {
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(User.class);
            } else {
                throw new MerchantDoesNotExist(response.readEntity(String.class));
            }
        }
    }
}
