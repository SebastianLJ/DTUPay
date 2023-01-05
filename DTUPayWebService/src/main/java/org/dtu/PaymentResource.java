package org.dtu;

import org.dtu.resources.Payment;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;


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
    public Response getPayment(@PathParam("id") int id) throws URISyntaxException {
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
    public Response getAmount(@PathParam("id") int id) {
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

    @Path("/{id}/cid")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getCid(@PathParam("id") int id) {
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

    @Path("/{id}/mid")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getMid(@PathParam("id") int id) {
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
            id = paymentRegistration.postPayment(payment);
            return Response.created(new URI("/payments/"+id))
                    .link(new URI("/payments/"+id), "self")
                    .link(new URI("/payments/"+id+"/amount"), "amount")
                    .link(new URI("/payments/"+id+"/cid"), "cid")
                    .link(new URI("/payments/"+id+"/mid"), "mid")
                    .build();
        } catch (PaymentAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("a payment with the id " + payment.getId() + " already exists")
                    .build();
        } catch (InvalidCustomerIdException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("customer with id " + payment.cid + " is unknown")
                    .build();
        } catch (InvalidMerchantIdException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("merchant with id " + payment.mid + " is unknown")
                    .build();
        }
    }

}
