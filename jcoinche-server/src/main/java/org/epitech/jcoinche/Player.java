package org.epitech.jcoinche;

import io.netty.channel.Channel;
import org.epitech.command.CommandProtos;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Player {

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
}
