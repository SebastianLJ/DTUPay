package org.dtu.facades;

import org.dtu.aggregate.User;
import org.dtu.exceptions.MerchantAlreadyExistsException;
import org.dtu.factories.MerchantFactory;
import org.dtu.repositories.MerchantRepository;
import org.dtu.services.MerchantService;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/merchants")
public class MerchantFacade {
    MerchantService merchantRegistration = new MerchantFactory().getService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/register")
    public Response registerMerchant(String firstName, String lastName, String bankAccount) {
        try {
            User newUser = merchantRegistration.addMerchant(firstName, lastName, bankAccount);
            return Response.status(Response.Status.CREATED)
                    .entity("merchant with id " + newUser.getUserId().getUuid() + " created")
                    .build();
        } catch (MerchantAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("merchant with the same id already exists")
                    .build();
        }
    }



}
