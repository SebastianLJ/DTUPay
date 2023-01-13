package org.dtu;

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

import java.util.List;

public class CustomerApp {
    Client c = ClientBuilder.newClient();
    WebTarget r = c.target("http://localhost:8080/");

    public User register(String firstName, String lastName, String bankNumber) throws Exception {
        User user = new User(firstName, lastName, bankNumber);
        Response response = r.path("customers")
                .request()
                //.accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(user, MediaType.APPLICATION_JSON));
        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            return response.readEntity(User.class);
        } else {
            System.out.println("!!!! " + response.readEntity(User.class) + " !!!!");
            throw new Exception("code: " + response.getStatus());
        }
    }


    public User deRegisterCustomer(User user) throws Exception {
        return r.path(
                        "customers/" + user.getUserId().getUuid()
                )
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .delete(User.class);

    }


    public List<Token> getTokens(UserId userId, int tokenCount) throws Exception {
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
            throw new Exception("code: " + response.getStatus());
        }
    }

    //TODO Implement exception
    public User getCustomer(User user) {
        return r.path("customer/" + user.getUserId().getUuid())
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);
//        if(response.getStatus() == Response.Status.OK.getStatusCode()) {
//            return response;
//        }
    }

}
