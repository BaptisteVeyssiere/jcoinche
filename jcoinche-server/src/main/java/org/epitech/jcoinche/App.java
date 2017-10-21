package org.epitech.jcoinche;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class App
{
    public static void main( String[] args ) throws Exception
    {
        int                 port;
        Server              server = null;
        ServerHandler       handler = null;
        ServerInitializer   initializer = new ServerInitializer();

        if (args.length != 1) {
            System.out.println("Usage: port");
            return;
        }
        port = Integer.parseInt(args[0]);
        EventLoopGroup  bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup  workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(initializer);
            Channel ch = b.bind(port).sync().channel();
            handler = initializer.getHandler();
            server = new Server(handler);
            server.loop();
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            System.out.println("Server closed gracefully");
        }
    }
}
