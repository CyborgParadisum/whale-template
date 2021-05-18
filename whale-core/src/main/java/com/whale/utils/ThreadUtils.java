package com.whale.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadUtils {
  public static ThreadPoolExecutor getThreadPoolExecutor() {
    ThreadFactory factory = new ThreadFactoryBuilder()
        .setDaemon(true)
        .setNameFormat("Executor task launch worker-%d")
        .build();
    return (ThreadPoolExecutor) Executors.newCachedThreadPool(factory);
  }
}
