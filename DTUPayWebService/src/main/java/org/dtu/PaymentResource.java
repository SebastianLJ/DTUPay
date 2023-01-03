package org.dtu;

import org.dtu.resources.Payment;

import javax.naming.directory.InvalidAttributeIdentifierException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;


@Path("/payments")
public class PaymentResource {
    PaymentRegistration paymentRegistration = new PaymentRegistration();
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Payment getPayment(@PathParam("id") Integer id) {
        try {
            return paymentRegistration.getPayment(id);
        } catch (PaymentNotFoundException e) {
            throw new NotFoundException("Payment not found");
        }
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postPayment(Payment payment) throws URISyntaxException, InvalidAttributeIdentifierException {
        int id;
        try {
            id = paymentRegistration.postPayment(payment);
            return Response.created(new URI("{..}/payments/"+id)).build();
        } catch (PaymentAlreadyExistsException e) {
            throw new InvalidAttributeIdentifierException();
        }
    }

}
