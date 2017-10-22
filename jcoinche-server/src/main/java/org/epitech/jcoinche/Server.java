package org.epitech.jcoinche;

import java.util.Vector;

public class Server {

    private ServerHandler   network;

    Server(ServerHandler handler) {
        network = handler;
    }

    public void loop() throws InterruptedException {

        Vector<Salon>   salons = new Vector<Salon>();

        while (true) {
            if (network.getClientNbr() > 3 && network.isAvailable()) {
                salons.add(network.makeSalon());
            }
            for (Salon salon : salons) {
                salon.playGame();
            }
            Thread.sleep(100);
        }
    }
}
