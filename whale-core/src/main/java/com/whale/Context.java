package com.whale;

import com.google.common.collect.ImmutableMap;
import com.whale.executor.Executor;
import com.whale.executor.ExecutorType;
import com.whale.executor.LocalExecutor;
import com.whale.scheduler.TaskIdentifier;
import com.whale.utils.ThreadUtils;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

public class Context {

  private static final ThreadPoolExecutor executorPools = ThreadUtils.getThreadPoolExecutor();


  /* jobManager和driver 目前仅提供 下发 task 的操作, 作用较为相似 */
  static {
    executorPools.execute(() -> {
      JobManager manager = getJobManager();
      manager.run(); /* run jobManager in driver */
    });
    executorPools.execute(() -> {
      Driver driver = getDriver();
      driver.run(); /* run driver here */
    });
  }

  private void init() {
    initExecutor(ExecutorType.LOCAL);
  }

  private Executor executor;


  /**
   * 启动 task 的计算，立意为 map,reduce等func。待实现
   */
  public void map(Supplier<TaskIdentifier> supplier) {
    executor.launchTask(supplier.get());
  }


  /**
   * 初始化 executor 调度器，用于分发 task 到对应的 executor中。 （立意有点问题目前）
   */
  private void initExecutor(ExecutorType type) {
    switch (type) {
      case LOCAL: {
        executor = new LocalExecutor();
      }
    }
  }

  private static final int port = 1234;
  private static final String hostName = "localhost";

  private static Driver getDriver() {
    return null;
  }
  private static JobManager getJobManager() {
    return new JobManager(ImmutableMap.of(
        "port", "414",
        "workers", "localhost:7001,localhost:7002"));
  }
}
