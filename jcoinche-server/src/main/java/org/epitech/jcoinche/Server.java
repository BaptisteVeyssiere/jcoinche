package org.epitech.jcoinche;

import java.util.Iterator;
import java.util.Vector;

public class Server {

    private ServerHandler   network;

    Server(ServerHandler handler) {
        network = handler;
    }

    public void loop() throws InterruptedException {

        Vector<Salon>   salons = new Vector<Salon>();
        Salon           tmp = null;


        while (true) {
            if (network.getClientNbr() > 3 && network.isAvailable()) {
                salons.add(network.makeSalon());
            }
            for (Iterator<Salon> salon = salons.iterator(); salon.hasNext(); ) {
                tmp = salon.next();
                if (tmp.getClientNbr() < 4) {
                    tmp.destroySalon();
                    salon.remove();
                }
            }
            try {
                for (Iterator<Salon> salon = salons.iterator(); salon.hasNext(); ) {
                    try {
                        tmp = salon.next();
                        tmp.playGame();
                    } catch (RuntimeException e) {
                        tmp.destroySalon();
                        salon.remove();
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            Thread.sleep(100);
        }
    }
}
