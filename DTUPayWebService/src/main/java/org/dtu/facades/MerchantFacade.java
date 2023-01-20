package org.dtu.facades;

import dtu.ws.fastmoney.BankServiceException_Exception;
import org.dtu.aggregate.Payment;
import org.dtu.aggregate.User;
import org.dtu.exceptions.*;
import org.dtu.factories.MerchantFactory;
import org.dtu.services.MerchantService;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Path("/merchants")
public class MerchantFacade {
    MerchantService service = new MerchantFactory().getService();

    /**
     * @author Sebastian Lund (s184209)
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getMerchant(@PathParam("id") String id) {
        UUID merchantId = UUID.fromString(id);
        try {
            User merchant = service.getMerchant(merchantId);
            return Response.status(Response.Status.OK)
                    .entity(merchant)
                    .build();
        } catch (InvalidMerchantIdException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * @author Sebastian Juste pedersen (s205335)
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerMerchant(User user) {
        try {
            User newUser = service.registerMerchant(user);
            return Response.status(Response.Status.OK)
                    .entity(newUser)
                    .build();
        } catch (MerchantAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("merchant with the same id already exists")
                    .build();
        }
    }


    /**
     * @author Noah Christiansen (s184186)
     */
    @POST
    @Path("/payments")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postPayment(Payment payment) throws URISyntaxException {
        try {
            UUID paymentID = service.createPayment(payment).getId();
            return Response.created(new URI("/payments/"+paymentID))
                    .link(new URI("/payments/"+paymentID), "self")
                    .link(new URI("/payments/"+paymentID+"/amount"), "amount")
                    .link(new URI("/payments/"+paymentID+"/cid"), "cid")
                    .link(new URI("/payments/"+paymentID+"/mid"), "mid")
                    .entity(payment)
                    .build();
        } catch (PaymentAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("a payment with the id " + payment.getId() + " already exists")
                    .build();
        } catch (CustomerNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("customer is unknown")
                    .build();
        } catch (InvalidMerchantIdException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("merchant is unknown")
                    .build();
        } catch (CustomerTokenAlreadyConsumedException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("token is invalid")
                    .build();
        } catch (BankServiceException_Exception | InvalidCustomerIdException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }

    }

    /**
     * @author Noah Christiansen (s184186)
     */

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deregister(@PathParam("id") String id) {
        try {
            UUID uuid = UUID.fromString(id);
            User deletedUser = service.deleteMerchant(uuid);
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
