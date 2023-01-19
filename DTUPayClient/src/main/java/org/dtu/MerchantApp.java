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
import org.dtu.exceptions.MerchantDoesNotExist;
import org.dtu.exceptions.PaymentDoesNotExist;

import java.util.List;
import java.util.UUID;

public class MerchantApp {
    Client c = ClientBuilder.newClient();
    WebTarget r = c.target("http://localhost:8080/");

    public User register(String firstName, String lastName, String bankNumber) throws Exception {
        User user = new User(firstName, lastName, bankNumber);
        Response response = r.path("merchants")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(user, MediaType.APPLICATION_JSON));
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(User.class);
        } else {
            System.out.println(response.getStatus() +" "+ Response.Status.OK.getStatusCode());
            System.out.println("!!!! " + response.readEntity(String.class) + " !!!!");
            throw new Exception("code: " + response.getStatus());
        }
    }

    public User deregister(User user) throws Exception {
        Response response = r.path("merchants/" + user.getUserId().getUuid())
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .delete();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(User.class);
        } else {
            throw new Exception("code: " + response.getStatus());
        }
    }

    public Payment pay(UserId merchantId, Token customerToken, int amount) throws Exception {
        Payment payment = new Payment(customerToken, merchantId.getUuid(), amount);
        Response response = r.path("merchants/payments")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(payment, MediaType.APPLICATION_JSON));
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Payment.class);
        } else {
            throw new Exception("code: " + response.getStatus());
        }
    }

    public User getMerchant(User user) throws MerchantDoesNotExist {
        Response response = r.path("merchants/" + user.getUserId().getUuid())
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(User.class);
        } else {
            throw new MerchantDoesNotExist(response.readEntity(String.class));
        }
    }

    public List<Payment> getMerchantReport(User user) throws PaymentDoesNotExist {
        Response response = r.path("reports/merchants/" + user.getUserId())
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<List<Payment>>() {
            });
        } else {
            throw new PaymentDoesNotExist();
        }
    }
}
