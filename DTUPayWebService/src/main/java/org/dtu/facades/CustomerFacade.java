package org.dtu.facades;

import org.dtu.aggregate.Token;
import org.dtu.aggregate.User;
import org.dtu.exceptions.CustomerAlreadyExistsException;
import org.dtu.exceptions.InvalidCustomerIdException;
import org.dtu.factories.CustomerFactory;
import org.dtu.services.CustomerService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Path("/customers")
public class CustomerFacade {
    CustomerService customerService = new CustomerFactory().getService();

    @Path("/{id}/{tokenCount}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTokens(@PathParam("id") String id) {
        try {
            UUID uuid = UUID.fromString(id);
            //todo use token generator
            Token token = new Token(uuid);
            return Response.status(Response.Status.OK)
                    .entity(token)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(String firstName, String lastName, String bankAccount) throws URISyntaxException {
        try {
            User user = customerService.addCustomer(firstName, lastName, bankAccount);
            return Response.status(Response.Status.CREATED)
                    .link(new URI("/"+user.getUserId().getUuid()+"/"+5), "tokens")
                    .entity("customer with id " + user.getUserId().getUuid() + " created")
                    .build();
        } catch (CustomerAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("customer with the same id already exists")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deregister(@PathParam("id") String id) {
        try {
            UUID uuid = UUID.fromString(id);
            User deletedUser = customerService.deleteCustomer(uuid);
            return Response
                    .status(Response.Status.OK)
                    .entity(deletedUser)
                    .build();
        } catch (IllegalArgumentException | InvalidCustomerIdException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
