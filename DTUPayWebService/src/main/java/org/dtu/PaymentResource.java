package org.dtu;

import org.dtu.resources.Payment;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;


@Path("/payments")
public class PaymentResource {
    PaymentRegistration paymentRegistration = new PaymentRegistration();

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
    public Response getPayment(@PathParam("id") Integer id) {
        try {
            return Response.status(Response.Status.OK)
                    .entity(paymentRegistration.getPayment(id))
                    .build();
        } catch (PaymentNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Payment with id " + id + " not found")
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postPayment(Payment payment) throws URISyntaxException {
        int id;
        try {
            id = paymentRegistration.postPayment(payment);
            return Response.created(new URI("/payments/"+id)).build();
        } catch (PaymentAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("A payment with the id " + payment.getId() + " already exists")
                    .build();
        } catch (InvalidCustomerIdException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid customer id")
                    .build();
        } catch (InvalidMerchantIdException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid merchant id")
                    .build();
        }
    }

}
