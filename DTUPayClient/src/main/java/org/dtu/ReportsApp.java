package org.dtu;

import aggregate.Payment;
import aggregate.User;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.dtu.exceptions.PaymentDoesNotExist;

import java.util.List;

public class ReportsApp {
    private final Client client;
    private final WebTarget webTarget;

    public ReportsApp(Client client, WebTarget webTarget) {
        this.client = client;
        this.webTarget = webTarget;
    }

    /**
     * @author Sebastian Juste pedersen (s205335)
     * @author Nicklas Olabi (s205347)
     */
    public List<Payment> getAllReports() throws PaymentDoesNotExist {
        try (Response response = webTarget.path("reports")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get()) {
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(new GenericType<>() {
                });
            } else {
                System.out.println(response.getStatus());
                throw new PaymentDoesNotExist();
            }
        }
    }

    /**
     * @author Sebastian Juste pedersen (s205335)
     * @author Nicklas Olabi (s205347)
     */
    public List<Payment> getMerchantReport(User user) throws PaymentDoesNotExist {
        try (Response response = webTarget.path("reports/merchant/" + user.getUserId().getUuid())
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get()) {
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(new GenericType<>() {
                });
            } else {
                System.out.println(response.getStatus());
                throw new PaymentDoesNotExist();
            }
        }
    }

    /**
     * @author Sebastian Juste pedersen (s205335)
     * @author Nicklas Olabi (s205347)
     */
    public List<Payment> getCustomerReport(User user) throws PaymentDoesNotExist {
        try (Response response = webTarget.path("reports/customer/" + user.getUserId().getUuid())
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get()) {
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(new GenericType<List<Payment>>() {
                });
            } else {
                System.out.println(response.getStatus());
                throw new PaymentDoesNotExist();
            }
        }
    }
}
