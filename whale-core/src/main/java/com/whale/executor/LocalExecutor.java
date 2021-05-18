package com.whale.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.whale.scheduler.TaskIdentifier;
import com.whale.utils.ThreadUtils;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

public class LocalExecutor implements Executor {

  private final ConcurrentHashMap<Long, Runnable> runningTasks = new ConcurrentHashMap<>();

  private final ThreadPoolExecutor threadPool = ThreadUtils.getThreadPoolExecutor();

  public void launchTask(TaskIdentifier task) {
    TaskRunner tr = new TaskRunner();
    runningTasks.put(task.getTaskId(), tr);
    threadPool.execute(tr);
  }

  class TaskRunner implements Runnable {

    private TaskIdentifier task;

    @Override
    public void run() {

    }
  }
}
