package com.whale.server;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.whale.RpcContext;
import com.whale.util.NettyUtils;
import com.whale.util.RpcConf;
import io.netty.buffer.PooledByteBufAllocator;
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

  private int port;

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
    if (conf.sharedByteBufAllocators()) {
      this.pooledAllocator = NettyUtils.getSharedPooledByteBufAllocator(
          conf.preferDirectBufForSharedByteBufAllocators(), true /* allow cache */);
    } else {
      this.pooledAllocator = NettyUtils.createPooledByteBufAllocator(
          conf.preferDirectBufs(), true /* allow cache */, conf.serverThreads());
    }
    this.bootstraps = Lists.newArrayList(Preconditions.checkNotNull(bootstraps));
  }

  public int getPort() {
    if (this.port == -1) {
      throw new IllegalStateException("server not initialized");
    }

    return this.port;
  }

}
