package org.epitech.jcoinche;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.epitech.command.CommandProtos;

import java.util.Vector;

@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<CommandProtos.Command> {

    Vector<Player>  clientqueue = new Vector<Player>();
    Vector<Player>  ingamequeue = new Vector<Player>();
    boolean         availability = true;

    ServerHandler() {
    }

    public int  getClientNbr() {
        return (clientqueue.size());
    }

    public boolean  isAvailable() {
        return (availability);
    }

    public Salon    makeSalon() {
        Vector<Player>  salon = new Vector<Player>();

        if (clientqueue.size() < 4 || availability == false)
            throw new RuntimeException("Can't make a new salon, not enough client");
        for (int i = 0; i < 4; ++i) {
            salon.add(clientqueue.remove(0));
            System.out.println("[" + salon.get(i) + "] has join a salon");
            ingamequeue.add(salon.get(i));
        }
        return (new Salon(salon.get(0), salon.get(1), salon.get(2), salon.get(3)));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();

        while (!availability) {
            Thread.sleep(100);
        }
        availability = false;
        clientqueue.add(new Player(incoming));
        System.out.println("[" + incoming.remoteAddress() + "] has join the server");
        availability = true;
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();

        while (!availability) {
            Thread.sleep(100);
        }
        availability = false;
        for (Player player : clientqueue)
            if (player.getChannel() == incoming) {
                clientqueue.remove(player);
                System.out.println("[" + incoming.remoteAddress() + "] has left the server");
                break;
            }
        for (Player player : ingamequeue)
            if (player.getChannel() == incoming) {
            player.setConnection(false);
                ingamequeue.remove(player);
                System.out.println("[" + incoming.remoteAddress() + "] has left the server");
                break;
            }
        availability = true;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, CommandProtos.Command command) throws Exception {
        CommandProtos.Command.Builder   builder = CommandProtos.Command.newBuilder();
        Channel                         incoming = ctx.channel();

        for (Player player : clientqueue)
            if (player.getChannel() == incoming) {
                System.out.println("[" + incoming.remoteAddress() + "]: " + command.getRequest());
                builder.setRequest("KO");
                ctx.write(builder.build());
                return ;
            }
        for (Player player : ingamequeue)
            if (player.getChannel() == incoming) {
                player.addCommand(command.getRequest());
                return ;
            }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
