package org.epitech.jcoinche;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.epitech.command.CommandProtos;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientHandler extends SimpleChannelInboundHandler<CommandProtos.Command> {

    private volatile Channel                            channel;
    private final BlockingQueue<CommandProtos.Command>  answer = new LinkedBlockingQueue<CommandProtos.Command>();
    private boolean                                     connected = false;

    public ClientHandler() {
        super(false);
    }

    public String    getCommand() {
        boolean interrupted = false;

        try {
            if (this.answer.size() > 0) {
                return (this.answer.take().getRequest());
            }
        } catch (InterruptedException ignore) {
            interrupted = true;
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        return (null);
    }

    public int  getCommandSize() {
        return (answer.size());
    }

    public void  sendCommand(String command) {
        CommandProtos.Command.Builder   builder = CommandProtos.Command.newBuilder();

        builder.setRequest(command);
        channel.writeAndFlush(builder.build());
    }

    public boolean  getStatus() {
        return (connected);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        connected = false;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        channel = ctx.channel();
        connected = true;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, CommandProtos.Command command) throws Exception {
        answer.add(command);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
