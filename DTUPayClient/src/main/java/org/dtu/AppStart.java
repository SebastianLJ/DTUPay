package org.dtu;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;

/**
 * @autor Jákup Viljam Dam - s185095
 */
public final class AppStart {

    private static AppStart instance;
    private final Client client = ClientBuilder.newClient();
    private final WebTarget webTarget = client.target("http://localhost:8080/");
    private final CustomerApp customerApp = new CustomerApp(client, webTarget);
    private final MerchantApp merchantApp = new MerchantApp(client, webTarget);
    private final ReportsApp reportsApp = new ReportsApp(client, webTarget);

    private AppStart() { }

    public static AppStart getInstance() {
        if (instance == null) {
            instance = new AppStart();
        }
        return instance;
    }

    public CustomerApp getCustomerApp() {
        return customerApp;
    }

    public MerchantApp getMerchantApp() {
        return merchantApp;
    }

    public ReportsApp getReportsApp() {
        return reportsApp;
    }
}
