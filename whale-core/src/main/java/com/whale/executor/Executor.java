package com.whale.executor;

import com.whale.scheduler.TaskIdentifier;

public interface Executor {
  void launchTask(TaskIdentifier task);

}
