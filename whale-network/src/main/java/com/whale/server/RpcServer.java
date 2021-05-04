package com.whale.server;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.whale.RpcContext;
import com.whale.util.NettyUtils;
import com.whale.util.RpcConf;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rpc server top abstraction
 */
public class RpcServer {
  // todo

  private static final Logger logger = LoggerFactory.getLogger(RpcHandler.class);

  private final RpcContext context;
  private final RpcConf conf;
  private final RpcHandler rpcHandler;

  private int port;

  private ServerBootstrap bootstrap;
  private final PooledByteBufAllocator pooledAllocator;
  private final List<RpcServerBootstrap> bootstraps;

  public RpcServer(
      RpcContext context,
      String host,
      int port,
      RpcHandler rpcHandler,
      List<RpcServerBootstrap> bootstraps) {
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
    this.bootstraps = Lists.newArrayList(Preconditions.checkNotNull(bootstraps));
  }

  private void init() {
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

        RpcHandler rpcHandler = RpcServer.this.rpcHandler;
        for (RpcServerBootstrap bootstrap : bootstraps) {
          rpcHandler = bootstrap.doBootstrap(ch, rpcHandler);
        }
      }
    });
  }

  public int getPort() {
    if (this.port == -1) {
      throw new IllegalStateException("server not initialized");
    }

    return this.port;
  }

}
