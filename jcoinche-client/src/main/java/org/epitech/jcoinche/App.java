package org.epitech.jcoinche;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.System;

public class App 
{
    public static void main( String[] args )
    {
        int     port = 0;
        String  host = null;
        String  line = null;

        if (args.length != 2) {
            System.out.println("Usage: host port");
            return;
        }

        try {
            host = args[0];
            port = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(84);
        }

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .handler(new ClientInitializer());
            Channel ch = b.connect(host, port).sync().channel();
            ClientHandler   handler = ch.pipeline().get(ClientHandler.class);
            BufferedReader  in = new BufferedReader(new InputStreamReader(System.in));

            while (handler.getStatus()) {
                if (in.ready()) {
                    line = in.readLine();
                    if (line.equals("quit") || !handler.getStatus())
                        break;
                    handler.sendCommand(line);
                }
                if (!handler.getStatus())
                    break;
                if (handler.getCommandSize() > 0)
                    System.out.println(handler.getCommand());
                Thread.sleep(100);
            }
            ch.close();
        } catch (Exception e) {
            System.out.println("Error encountered: " + e.getMessage());
        } finally {
            group.shutdownGracefully();
            System.out.println("Client closed gracefully");
        }
    }
}
