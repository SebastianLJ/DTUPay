package org.dtu.facades;

import org.dtu.aggregate.Payment;
import org.dtu.aggregate.User;
import org.dtu.exceptions.*;
import org.dtu.factories.MerchantFactory;
import org.dtu.factories.PaymentFactory;
import org.dtu.repositories.MerchantRepository;
import org.dtu.services.MerchantService;
import org.dtu.services.PaymentService;

import javax.ws.rs.*;
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
    @Path("/{id}")
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
        try {
            payment = paymentRegistration.createPayment(payment);
            return Response.created(new URI("/payments/"+payment.getId()))
                    .link(new URI("/payments/"+payment.getId()), "self")
                    .link(new URI("/payments/"+payment.getId()+"/amount"), "amount")
                    .link(new URI("/payments/"+payment.getId()+"/cid"), "cid")
                    .link(new URI("/payments/"+payment.getId()+"/mid"), "mid")
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
    @Path("/{id}")
    public Response deregister(@PathParam("id") String id) {
        try {
            UUID uuid = UUID.fromString(id);
            User deletedUser = merchantRegistration.deleteMerchant(uuid);
            return Response
                    .status(Response.Status.OK)
                    .entity(deletedUser)
                    .build();
        } catch (IllegalArgumentException | InvalidMerchantIdException | PaymentNotFoundException | MerchantNotFoundException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }



}
