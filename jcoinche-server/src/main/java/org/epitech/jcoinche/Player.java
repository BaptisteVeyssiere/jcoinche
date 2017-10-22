package org.epitech.jcoinche;

import io.netty.channel.Channel;
import org.epitech.command.CommandProtos;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Player {

    private Vector<Card>            cards = new Vector<Card>();
    private Channel                 channel;
    private BlockingQueue<String>   commands = new LinkedBlockingQueue<String>();
    private boolean                 connected = true;

    Player(Channel ch) {
        channel = ch;
    }

    public int  getCommandNbr() {
        return (commands.size());
    }

    public String   getCommand() throws InterruptedException {

        if (commands.size() < 1)
            return (null);
        return (commands.take());
    }

    public Channel  getChannel() {
        return (channel);
    }

    public void addCommand(String command) {
        commands.add(command);
        System.out.println("New command added: " + command);
    }

    public void sendCommand(String command) {
        CommandProtos.Command.Builder   builder = CommandProtos.Command.newBuilder();

        builder.setRequest(command);
        channel.writeAndFlush(builder.build());
    }

    public void     setConnection(boolean status) {
        connected = status;
    }

    public boolean  getConnection() {
        return (connected);
    }

    public void clearCards() {
        cards.clear();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void sendCardList() {
        sendCommand("Voici vos cartes:");
        for (int i = 0 ; i < cards.size() ; ++i) {
            sendCommand(cards.elementAt(i).getName());
        }
    }

    public Card getCardByName(String name) {
        for (Card card : cards) {
            if (card.getName().equals(name))
                return (card);
        }
        throw new RuntimeException();
    }

    public void removeCardByName(String name) {
        for (Card card : cards) {
            if (card.getName().equals(name)) {
                cards.remove(card);
                break;
            }
        }
    }

    public boolean hasBetterCardSameColor(int value, int color, int valueColor) {
        for (Card card : cards) {
            if (card.getColor() == color && card.getValue(valueColor) > value)
                return (true);
        }
        return (false);
    }

    public int getCardNbr() {
        return (cards.size());
    }
}
