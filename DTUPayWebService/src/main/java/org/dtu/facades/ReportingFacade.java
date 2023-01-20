package org.dtu.facades;

import org.dtu.aggregate.Payment;
import org.dtu.aggregate.UserId;
import org.dtu.exceptions.PaymentNotFoundException;
import org.dtu.factories.ReportFactory;
import org.dtu.services.ReportService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

/**
 * @author Sebastian Juste pedersen (s205335)
 * @author Nicklas Olabi (s205347)
 */
@Path("/reports")
public class ReportingFacade {

    ReportService reportService = new ReportFactory().getService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPayments() {
        try {
            List<Payment> payments = reportService.getPayments();
            return  Response
                    .status(Response.Status.OK)
                    .entity(payments)
                    .build();
        } catch(PaymentNotFoundException e){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @Path("/merchant/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaymentsByMerchantId(@PathParam("id") String id) {
        try {
            UUID uuid = UUID.fromString(id);
            UserId userid = new UserId(uuid);
            List<Payment> merchantPayments = reportService.getPaymentByMerchantId(userid);
            return  Response
                    .status(Response.Status.OK)
                    .entity(merchantPayments)
                    .build();
        } catch(PaymentNotFoundException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @Path("/customer/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaymentsByCustomerId(@PathParam("id") String id) {
        UUID uuid = UUID.fromString(id);
        UserId userid = new UserId(uuid);
        List<Payment> merchantPayments = reportService.getPaymentByCustomerId(userid);
        return  Response
                .status(Response.Status.OK)
                .entity(merchantPayments)
                .build();
    }


}
