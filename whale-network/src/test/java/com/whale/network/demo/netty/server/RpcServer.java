package com.whale.network.demo.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.io.Closeable;
import java.io.IOException;

public class RpcServer implements Closeable {
  private String hostName;
  private int port;

  public RpcServer(String hostName, int port) {
    this.hostName = hostName;
    this.port = port;
  }

  private ServerBootstrap bootstrap;
  private ChannelFuture bind;
  private NioEventLoopGroup eventLoopGroup;

  public void startServer() throws InterruptedException {
    System.out.println("server start init first");
    bootstrap = new ServerBootstrap();
    eventLoopGroup = new NioEventLoopGroup();
    bootstrap.group(eventLoopGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline p = ch.pipeline();
            p.addLast(new StringDecoder());
            p.addLast(new StringEncoder());
            p.addLast(new HelloServerHandler());
          }
        });
    bind = bootstrap.bind(hostName, port);
    System.out.println("server bind sync start");
    bind.sync();
  }


  @Override
  public void close() throws IOException {
    eventLoopGroup.shutdownGracefully();
  }
}
