package org.dtu;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/registration")
public class RegistrationResource {
    CustomerService customerRegistration = new CustomerService();
    MerchantService merchantRegistration = new MerchantService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/customers")
    public Response addCustomer(String id) {
        try {
            customerRegistration.addCustomer(id);
            return Response.status(Response.Status.CREATED)
                    .entity("customer with id " + id + " created")
                    .build();
        } catch (CustomerAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("customer with id " + id + " already exists")
                    .build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/merchants")
    public Response addMerchant(String id) {
        try {
            merchantRegistration.addMerchant(id);
            return Response.status(Response.Status.CREATED)
                    .entity("merchant with id " + id + " created")
                    .build();
        } catch (MerchantAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("merchant with id " + id + " already exists")
                    .build();
        }
    }
}
