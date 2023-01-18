package org.dtu;

import org.dtu.factories.TokenFactory;

public class StartUp {
    public static void main(String[] args) {
        new StartUp().startTokenManagement();
    }

    private void startTokenManagement() {
        new TokenFactory().getService();
    }
}