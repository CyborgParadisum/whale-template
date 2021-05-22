package com.whale.util;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.ThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyUtils {

  private static final Logger LOG = LoggerFactory.getLogger(NettyUtils.class);

  /**
   *
   */
  private static final int MAX_DEFAULT_NETTY_THREADS = 8;
  private static final PooledByteBufAllocator[] _sharedPooledByteBufAllocator =
      new PooledByteBufAllocator[2];

  public static PooledByteBufAllocator createPooledByteBufAllocator(
      boolean allowDirectBufs,
      boolean allowCache,
      int coreNums) {
    if (coreNums <= 0) {
      LOG.debug(String.format("createPoolByteBufferAllocator coreNums is:%s", coreNums));
      coreNums = Runtime.getRuntime().availableProcessors();
    }

    boolean preferDirect = allowDirectBufs && PlatformDependent.directBufferPreferred();
    int nHeapArena = Math.min(PooledByteBufAllocator.defaultNumHeapArena(), coreNums);
    int nDirectArena = Math.min(PooledByteBufAllocator.defaultNumDirectArena(),
        allowDirectBufs ? coreNums : 0);
    int pageSize = PooledByteBufAllocator.defaultPageSize();
    int maxOrder = PooledByteBufAllocator.defaultMaxOrder();

    int tinyCacheSize = allowCache ? PooledByteBufAllocator.defaultTinyCacheSize() : 0;
    int smallCacheSize = allowCache ? PooledByteBufAllocator.defaultSmallCacheSize() : 0;
    int normalCacheSize = allowCache ? PooledByteBufAllocator.defaultNormalCacheSize() : 0;
    boolean useCacheForAllThreads =
        allowCache && PooledByteBufAllocator.defaultUseCacheForAllThreads();

    return new PooledByteBufAllocator(
        preferDirect, nHeapArena, nDirectArena, pageSize, maxOrder,
        tinyCacheSize, smallCacheSize, normalCacheSize,
        useCacheForAllThreads
    );
  }

  /**
   * implement epoll firstly, futhermore, implement NIO as soon .
   *
   * @param threadNumbers    ths numbers of thread
   * @param threadPoolPrefix name of thread pool
   * @return
   */
  public static EventLoopGroup createEventLoopGroup(int threadNumbers, String threadPoolPrefix) {
    ThreadFactory threadFactory = createThreadFactory(threadPoolPrefix);
    return new NioEventLoopGroup(threadNumbers, threadFactory);
  }

  /**
   * Epoll implementation firstly. further add other Server channel:
   * e.g.: NIO implementation
   * @return Server channel
   */
  public static Class<? extends ServerChannel> getServerChannel() {
    return NioServerSocketChannel.class;
  }

  public static ThreadFactory createThreadFactory(String threadPoolPrefix) {
    return new DefaultThreadFactory(threadPoolPrefix, true);
  }

  public static synchronized PooledByteBufAllocator
  getSharedPooledByteBufAllocator(
      boolean allowDirectBuf,
      boolean allowCache) {
    final int index = allowCache ? 0 : 1;
    if (_sharedPooledByteBufAllocator[index] == null) {
      _sharedPooledByteBufAllocator[index] =
          createPooledByteBufAllocator(allowDirectBuf,
              allowCache, defaultThreadNum(0));
    }
    return _sharedPooledByteBufAllocator[index];
  }

  private static int defaultThreadNum(int usableThreadCoreNum) {
    int availableCores = usableThreadCoreNum > 0 ?
        usableThreadCoreNum : Runtime.getRuntime().availableProcessors();
    return Math.min(availableCores, MAX_DEFAULT_NETTY_THREADS);
  }

  /** Returns the remote address on the channel or "&lt;unknown remote&gt;" if none exists. */
  public static String getRemoteAddress(Channel channel) {
    if (channel != null && channel.remoteAddress() != null) {
      return channel.remoteAddress().toString();
    }
    return "<unknown remote>";
  }

}
