package org.dtu;

import org.dtu.aggregate.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/registration")
public class RegistrationResource {
    CustomerService customerRegistration = new CustomerService();
    MerchantService merchantRegistration = new MerchantService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/customers")
    public Response addCustomer(String firstName, String lastName) {
        User newUser;
        try {
            newUser = customerRegistration.addCustomer(firstName, lastName);
            return Response.status(Response.Status.CREATED)
                    .entity("customer with id " + newUser.getUserId().getUuid() + " created")
                    .build();
        } catch (CustomerAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("customer with the same id already exists")
                    .build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/merchants")
    public Response addMerchant(String firstName, String lastName) {
        try {
            User newUser = merchantRegistration.addMerchant(firstName, lastName);
            return Response.status(Response.Status.CREATED)
                    .entity("merchant with id " + newUser.getUserId().getUuid() + " created")
                    .build();
        } catch (MerchantAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("merchant with the same id already exists")
                    .build();
        }
    }

    @Path("/cid/{id}/")
    @GET
    public Response getCustomer(@PathParam("id") UUID id) {
        try {
            customerRegistration.getCustomer(id);
            return Response.status(Response.Status.OK)
                    .build();
        } catch (InvalidCustomerIdException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("customer with id " + id + " not found")
                    .build();
        }
    }

    @Path("mid/{id}/")
    @GET
    public Response getMerchant(@PathParam("id") UUID id) {
        try {
            merchantRegistration.getMerchant(id);
            return Response.status(Response.Status.OK)
                    .build();
        } catch (InvalidMerchantIdException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("merchant with id " + id + " not found")
                    .build();
        }
    }
}
