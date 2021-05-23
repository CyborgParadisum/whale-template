package com.whale.client;

import com.google.common.base.Preconditions;
import com.whale.RpcContext;
import com.whale.server.RpcChannelHandler;
import com.whale.util.NettyUtils;
import com.whale.util.RpcConf;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * maintain Rpc client instance. pooling clients
 */
public class RpcClientFactory implements Closeable {
  private static final Logger logger = LoggerFactory.getLogger(RpcClientFactory.class);

  private RpcContext context;
  private RpcConf conf;

  private final PooledByteBufAllocator pooledByteBufAllocator;

  private final Class<? extends Channel> clientChannelClazz;
  private final EventLoopGroup workerGroup;

  RpcClientFactory(RpcContext ctx) {
    this.context = Preconditions.checkNotNull(ctx);
    this.conf = Preconditions.checkNotNull(ctx.getConf());

    clientChannelClazz = NettyUtils.getClientChannel();
    workerGroup = NettyUtils.createEventLoopGroup(
        conf.getNumsClientThread(), "client");
    pooledByteBufAllocator = NettyUtils.createPooledByteBufAllocator(
        conf.preferDirectBufs(), false /* allowCache */, conf.getNumsClientThread());
  }


  RpcClient createClient(InetSocketAddress address) throws InterruptedException, IOException {
    logger.debug("Creating new connection to {}", address);

    RpcClient client = null;

    Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(workerGroup)
        .channel(clientChannelClazz)
        .option(ChannelOption.TCP_NODELAY, true)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, conf.getClientConnectionTimeout())
        .option(ChannelOption.ALLOCATOR, pooledByteBufAllocator);

    bootstrap.handler(new ChannelInitializer<SocketChannel>() {
      @Override
      protected void initChannel(SocketChannel ch) {
        context.initializePipeline(ch);
      }
    });

    ChannelFuture f = bootstrap.connect(address);
    if (!f.await(conf.getClientConnectionTimeout())) {
      throw new IOException(
          String.format("Connecting to %s timed out (%s ms)", address,
              conf.getClientConnectionTimeout()));
    } else if (f.cause() != null) {
      throw new IOException(String.format("Failed to connect to %s", address), f.cause());
    }

    return client;
  }

  @Override
  public void close() {
    if (workerGroup != null && !workerGroup.isShuttingDown()) {
      workerGroup.shutdownGracefully();
    }
  }

}

