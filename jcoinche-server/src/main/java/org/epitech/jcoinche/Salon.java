package org.epitech.jcoinche;

import io.netty.channel.Channel;

import java.util.Vector;

public class Salon {

    private GameManager     gameMng;
    private Vector<Player>  players = new Vector<Player>();

    Salon(Player player1, Player player2, Player player3, Player player4) {
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        gameMng = new GameManager(players);
    }

    public void playGame() {
        gameMng.continueGame();
    }

    public int  getCommandNbr(int id) {
        if (players.size() <= id)
            throw new RuntimeException("Bad player ID requested");
        return (players.get(id).getCommandNbr());
    }

    public String   getCommand(int id) throws InterruptedException {
        if (players.size() <= id)
            throw new RuntimeException("Bad player ID requested");
        return (players.get(id).getCommand());
    }

    public Channel getPlayer(int id) {
        if (players.size() <= id)
            throw new RuntimeException("Bad player ID requested");
        return (players.get(id).getChannel());
    }

    public void sendCommand(int id, String command) {
        if (players.size() <= id)
            throw new RuntimeException("Bad player ID requested");
        players.get(id).sendCommand(command);
    }
}
