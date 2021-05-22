package com.whale.server;

import com.whale.RpcContext;
import com.whale.util.NettyUtils;
import com.whale.util.RpcConf;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rpc server top abstraction
 */
public class RpcServer implements Closeable {
  // todo

  private static final Logger logger = LoggerFactory.getLogger(RpcChannelHandler.class);

  private final RpcContext context;
  private final RpcConf conf;
  private final RpcHandler rpcHandler;

  private ServerBootstrap bootstrap;
  private final PooledByteBufAllocator pooledAllocator;

  private ChannelFuture channelFuture; /* 维持与client的channel */

  public RpcServer(
      RpcContext context,
      String host,
      int port,
      RpcHandler rpcHandler) {
    this.context = context;
    this.conf = context.getConf();
    this.rpcHandler = rpcHandler;
    if (conf.sharedByteBufAllocators()) {
      this.pooledAllocator = NettyUtils.getSharedPooledByteBufAllocator(
          conf.preferDirectBufForSharedByteBufAllocators(), true /* allow cache */);
    } else {
      this.pooledAllocator = NettyUtils.createPooledByteBufAllocator(
          conf.preferDirectBufs(), true /* allow cache */, conf.serverThreads());
    }

    boolean shouldClose = true;
    try {
      init(host, port);
      shouldClose = false;
    } finally {
      if (shouldClose) {
        try {
          this.close();
        } catch (IOException e) {
          logger.error("IOException should not have been thrown.", e);
        }
      }
    }

  }

  private void init(String hostToBind, int portToBind) {
    String moduleName = "server";
    EventLoopGroup boosGroup = NettyUtils.createEventLoopGroup(1,
        moduleName + "-boos");
    EventLoopGroup workerGroup = NettyUtils.createEventLoopGroup(conf.serverThreads(),
        moduleName + "-worker");

    bootstrap = new ServerBootstrap()
        .group(boosGroup, workerGroup)
        .channel(NettyUtils.getServerChannel())
        .option(ChannelOption.ALLOCATOR, pooledAllocator)
        .childOption(ChannelOption.ALLOCATOR, pooledAllocator);

    bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
      @Override
      protected void initChannel(SocketChannel ch) throws Exception {
        logger.info("New connection accepted for remote address {}", ch.remoteAddress());
        context.initializePipeline(ch, rpcHandler);
      }
    });

    InetSocketAddress address = getAddress(hostToBind, portToBind);

    channelFuture = bootstrap.bind(address);
    channelFuture.syncUninterruptibly();

    int port = ((InetSocketAddress) channelFuture.channel().localAddress()).getPort();
    logger.warn("rpc server start on port: {}", port);
  }

  public void syncCloseFuture() throws InterruptedException {
    logger.info("syncCloseFuture");
    channelFuture.channel().closeFuture().sync();
  }

  private InetSocketAddress getAddress(String hostToBind, int portToBind) {
    if (hostToBind == null) {
      return new InetSocketAddress(portToBind);
    } else {
      return new InetSocketAddress(hostToBind, portToBind);
    }
  }

  @Override
  public void close() throws IOException {
    if (Objects.nonNull(channelFuture)) {
      channelFuture.channel().close().awaitUninterruptibly(10, TimeUnit.SECONDS);
      channelFuture = null;
    }
    if (Objects.nonNull(bootstrap)) {
      bootstrap.config().group().shutdownGracefully();
    }
    if (Objects.nonNull(bootstrap)
        && Objects.nonNull(bootstrap.config().childGroup())) {
      bootstrap.config().childGroup().shutdownGracefully();
    }

    bootstrap = null;
  }
}
