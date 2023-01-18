package org.dtu;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.dtu.factories.TokenFactory;

@QuarkusMain
public class StartUp {
    public static void main(String... args) {
        Quarkus.run(MyApp.class, args);
    }
    private void startTokenManagement() {
        new TokenFactory().getService();
    }

    public static class MyApp implements QuarkusApplication {

        @Override
        public int run(String... args) throws Exception {
            new StartUp().startTokenManagement();
            Quarkus.waitForExit();
            return 0;
        }
    }
}