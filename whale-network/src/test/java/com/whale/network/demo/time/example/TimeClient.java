package com.whale.network.demo.time.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {
  private final String hostName;
  private final int port;

  public TimeClient(String hostName, int port) {
    this.hostName = hostName;
    this.port = port;
  }

  public void start() {
    NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      Bootstrap b = new Bootstrap();
      b.group(workerGroup)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.SO_KEEPALIVE, true)
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
              System.out.println("initChannel SocketChannel:" + ch);
              ch.pipeline().addLast(new TimeClientHandler());
            }
          });

      // start a client

      ChannelFuture f = b.connect(hostName, port).sync();

      // wait until the connection is closed
      f.channel().closeFuture().sync();

    } catch (InterruptedException e) {
      System.out.println("exit shutdownGracefully");
      workerGroup.shutdownGracefully();
    }
  }
}
