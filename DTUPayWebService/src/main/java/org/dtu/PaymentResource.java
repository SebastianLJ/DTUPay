package org.dtu;

import org.dtu.aggregate.Payment;
import org.dtu.services.PaymentService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;


@Path("/payments")
public class PaymentResource {
    PaymentService paymentRegistration = new PaymentService();

    static RegistrationResource registrationResource = new RegistrationResource();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPayments() {
        return Response.status(Response.Status.OK)
                .entity(paymentRegistration.getPayments())
                .build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPayment(@PathParam("id") UUID id) throws URISyntaxException {

        try {
            return Response.status(Response.Status.OK)
                    .entity(paymentRegistration.getPayment(id))
                    .link(new URI("/payments/"+id), "self")
                    .link(new URI("/payments/"+id+"/amount"), "amount")
                    .link(new URI("/payments/"+id+"/cid"), "cid")
                    .link(new URI("/payments/"+id+"/mid"), "mid")
                    .build();
        } catch (PaymentNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("payment with id " + id + " not found")
                    .build();
        }
    }

    @Path("/{id}/amount")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAmount(@PathParam("id") UUID id) {
        try {
            return Response.status(Response.Status.OK)
                    .entity(paymentRegistration.getPayment(id).amount)
                    .build();
        } catch (PaymentNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("payment with id " + id + " not found")
                    .build();
        }
    }

    @Path("/cid/{id}/")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getCid(@PathParam("id") UUID id) {
        try {
            return Response.status(Response.Status.OK)
                    .entity(paymentRegistration.getPayment(id).cid)
                    .build();
        } catch (PaymentNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("payment with id " + id + " not found")
                    .build();
        }
    }

    @Path("/mid/{id}/")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getMid(@PathParam("id") UUID id) {
        try {
            return Response.status(Response.Status.OK)
                    .entity(paymentRegistration.getPayment(id).mid)
                    .build();
        } catch (PaymentNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("payment with id " + id + " not found")
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

}
