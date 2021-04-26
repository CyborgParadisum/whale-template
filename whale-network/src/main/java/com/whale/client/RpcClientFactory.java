package com.whale.client;

import com.google.common.base.Preconditions;
import com.whale.RpcContext;
import com.whale.util.NettyUtils;
import com.whale.util.RpcConf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * maintain Rpc client instance.
 * pooling clients
 */
public class RpcClientFactory {
  private static final Logger LOG = LoggerFactory.getLogger(RpcClientFactory.class);

  private RpcContext context;
  private RpcConf conf;

  private final PooledByteBufAllocator pooledByteBufAllocator;

  RpcClientFactory(RpcContext ctx) {
    this.context = Preconditions.checkNotNull(ctx);
    this.conf = Preconditions.checkNotNull(ctx.getConf());

    EventLoopGroup workerGroup = NettyUtils
        .createEventLoopGroup(conf.getNumsClientThread(), "client");
    pooledByteBufAllocator = NettyUtils.createPooledByteBufAllocator(
        conf.preferDirectBufs(), false /* allowCache */, conf.getNumsClientThread());
  }


}

