package com.whale.util;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.ThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyUtils {

  private static final Logger LOG = LoggerFactory.getLogger(NettyUtils.class);

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
    return new EpollEventLoopGroup(threadNumbers);
  }

  public static ThreadFactory createThreadFactory(String threadPoolPrefix) {
    return new DefaultThreadFactory(threadPoolPrefix, true);
  }
}
