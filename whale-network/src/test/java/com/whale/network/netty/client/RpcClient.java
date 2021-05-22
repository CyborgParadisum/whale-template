package com.whale.network.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcClient {

  public static final String providerName = "HelloService#hello#";

  private static ExecutorService executors = Executors
      .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  private static NioEventLoopGroup eventLoopGroup;
  private static HelloClientHandler clientHandler;

  private static Bootstrap initClient() {
    clientHandler = new HelloClientHandler();

    eventLoopGroup = new NioEventLoopGroup();
    Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(eventLoopGroup)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.TCP_NODELAY, true)
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          public void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline p = ch.pipeline();
            p.addLast(new StringDecoder());
            p.addLast(new StringEncoder());
            p.addLast(clientHandler);
          }
        });

    return bootstrap;
  }


  public Channel connect(String hostName, int port) throws InterruptedException {
    Bootstrap bootstrap = initClient();
    ChannelFuture channelFuture = bootstrap.connect(hostName, port);
    if (channelFuture.isSuccess()) {
      System.out.println("connect success");
    }
    return channelFuture.channel();
  }

  public static <T> T send(Channel channel, String msg) throws InterruptedException {
    channel.writeAndFlush(msg).await();
    return (T) clientHandler.getResult();
  }

}
