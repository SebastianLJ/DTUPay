package org.dtu.facades;

import org.dtu.aggregate.Payment;
import org.dtu.aggregate.User;
import org.dtu.exceptions.*;
import org.dtu.factories.MerchantFactory;
import org.dtu.factories.PaymentFactory;
import org.dtu.repositories.MerchantRepository;
import org.dtu.services.MerchantService;
import org.dtu.services.PaymentService;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Path("/merchants")
public class MerchantFacade {
    MerchantService merchantRegistration = new MerchantFactory().getService();
    PaymentService paymentRegistration = new PaymentFactory().getService();

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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postPayment(Payment payment) throws URISyntaxException {
        int id;
        try {
            payment = paymentRegistration.createPayment(payment);
            return Response.created(new URI("/payments/"+payment.id))
                    .link(new URI("/payments/"+payment.id), "self")
                    .link(new URI("/payments/"+payment.id+"/amount"), "amount")
                    .link(new URI("/payments/"+payment.id+"/cid"), "cid")
                    .link(new URI("/payments/"+payment.id+"/mid"), "mid")
                    .build();
        } catch (PaymentAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("a payment with the id " + payment.getId() + " already exists")
                    .build();
        } catch (InvalidCustomerIdException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("customer is unknown")
                    .build();
        } catch (InvalidMerchantIdException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("merchant is unknown")
                    .build();
        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/unregister")
    public Response deleteMerchant(UUID id) throws MerchantNotFoundException, PaymentNotFoundException, InvalidMerchantIdException {
        User deletedUser = merchantRegistration.deleteMerchant(id);
        return Response.status(Response.Status.CREATED)
                    .entity("merchant with id " + deletedUser.getUserId().getUuid() + " has been unregistered")
                    .build();
    }



}
