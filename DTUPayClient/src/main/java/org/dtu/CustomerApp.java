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
import java.util.UUID;

public class CustomerApp {
    Client c = ClientBuilder.newClient();
    WebTarget r = c.target("http://localhost:8080/");

    /**
     * @author Sebastian Lund (s184209)
     */
    public User register(String firstName, String lastName, String bankNumber) throws Exception {
        User user = new User(firstName, lastName, bankNumber);
        Response response = r.path("customers")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(user, MediaType.APPLICATION_JSON));
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(User.class);
        } else {
            System.out.println("!!!! " + response.readEntity(String.class) + " !!!!");
            throw new Exception("code: " + response.getStatus());
        }
    }


    public User deRegisterCustomer(User user) throws Exception {
        Response response = r.path("customers/" + user.getUserId().getUuid())
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .delete();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(User.class);
        } else {
            throw new Exception("code: " + response.getStatus());
        }
    }

    /**
     * @author Sebastian Lund (s184209)
     */
    public List<Token> generateTokens(UserId userId, int tokenCount) throws Exception {
        Response response = r.path(
                        "customers/"+
                        userId.getUuid()+
                        "/tokens"
                ).request()
                .accept(MediaType.APPLICATION_JSON)
                .put(Entity.entity(tokenCount, MediaType.APPLICATION_JSON));
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<List<Token>>() {
            });
        } else {
            throw new Exception("code: " + response.getStatus() + "\n" + response.readEntity(String.class));
        }
    }

    public User getCustomer(User user) throws CustomerDoesNotExist {
        Response response = r.path("customers/" + user.getUserId().getUuid())
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(User.class);
        } else {
            throw new CustomerDoesNotExist();
        }
    }

    public List<Payment> getCustomerReport(User user) throws PaymentDoesNotExist {
        Response response = r.path("reports/customer/"+user.getUserId())
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
