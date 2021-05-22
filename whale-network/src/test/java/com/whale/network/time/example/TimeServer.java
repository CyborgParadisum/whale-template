package com.whale.network.time.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {
  private final String hostName;
  private final int port;

  public TimeServer(String hostName, int port) {
    this.hostName = hostName;
    this.port = port;
  }

  public void start() throws InterruptedException {
    System.out.println("ready to start");
    NioEventLoopGroup boosGroup = new NioEventLoopGroup();
    NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(boosGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new TimeServerHandler());
          }
        })
        .option(ChannelOption.SO_BACKLOG, 128)
        .option(ChannelOption.SO_KEEPALIVE, true);

    ChannelFuture future = bootstrap.bind(hostName, port);
    future.channel().closeFuture().sync();


  }
}
