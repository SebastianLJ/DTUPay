package org.dtu;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;

public class AppStart {

    private static final AppStart instance = new AppStart();
    private final Client client = ClientBuilder.newClient();
    private final WebTarget webTarget = client.target("http://localhost:8080/");
    private final CustomerApp customerApp = new CustomerApp(client, webTarget);
    private final MerchantApp merchantApp = new MerchantApp(client, webTarget);
    private final ReportsApp reportsApp = new ReportsApp(client, webTarget);

    private AppStart() { }

    public static AppStart getInstance() {
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
