package org.dtu.facades;

import org.dtu.aggregate.UserId;
import org.dtu.domain.Token;
import org.dtu.aggregate.User;
import org.dtu.exceptions.CustomerAlreadyExistsException;
import org.dtu.exceptions.CustomerNotFoundException;
import org.dtu.exceptions.InvalidCustomerIdException;
import org.dtu.exceptions.InvalidCustomerNameException;
import org.dtu.factories.CustomerFactory;
import org.dtu.services.CustomerService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.UUID;


@Path("/customers")
public class CustomerFacade {
    CustomerService customerService = new CustomerFactory().getService();

    /**
     * @author Sebastian Lund (s184209)
     */
    @Path("/{id}/tokens")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTokens(@PathParam("id") String id, int tokenCount) {
        try {
            UUID uuid = UUID.fromString(id);
            //todo use token generator
            ArrayList<Token> tokens = customerService.getTokens(new UserId(uuid), tokenCount);
            return Response.status(Response.Status.OK)
                    .entity(tokens)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            for (StackTraceElement el:
                 e.getStackTrace()) {
                System.out.println(el.toString());
            }
            System.out.println(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * @author Sebastian Lund (s184209)
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(User user) throws URISyntaxException {
        try {
            System.out.println("Adding customer");
            User createdUser = customerService.addCustomer(user);
            System.out.println("User: " + createdUser);
            return Response.ok(Response.Status.CREATED)
                    .entity(createdUser)
                    .build();
        } catch (CustomerAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("customer with the same id already exists")
                    .build();
        } catch (InvalidCustomerNameException e) {
            System.out.println("Invalid customer");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("invalid customer object")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(e.getMessage())
                    .build();
        }
    }


    /**
     * @author Noah Christiansen (s184186)
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response deregister(@PathParam("id") String id) {
        try {
            System.out.println("Deleting customer");
            UUID uuid = UUID.fromString(id);
            User deletedUser = customerService.deleteCustomer(customerService.getCustomer(uuid));
            System.out.println("customer: " + uuid);
            return Response
                    .status(Response.Status.OK)
                    .entity(deletedUser)
                    .build();
        } catch (IllegalArgumentException | CustomerNotFoundException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * @author Noah Christiansen (s184186)
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomer(@PathParam("id") String id) {
        try {
            System.out.println("Getting customer");
            UUID uuid = UUID.fromString(id);
            User user = customerService.getCustomer(uuid);
            System.out.println("Found customer with uuid: " + uuid);
            return  Response
                    .status(Response.Status.OK)
                    .entity(user)
                    .build();
        } catch (CustomerNotFoundException | IllegalArgumentException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * @author Sebastian Lund (s184209)
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getALlCustomers() {
        return Response.status(Response.Status.OK)
                .entity(customerService.getCustomerList())
                .build();
    }
}