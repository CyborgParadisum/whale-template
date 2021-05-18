package com.whale.scheduler;

public class TaskIdentifier {
  private Long taskId;
  private Object serializedTask; /* serialized task of has been deserialized task */

  public TaskIdentifier(Long taskId, Object serializedTask) {
    this.taskId = taskId;
    this.serializedTask = serializedTask;
  }

  public Long getTaskId() {
    return taskId;
  }
  public Object getSerializedTask() {
    return serializedTask;
  }

  @Override
  public String toString() {
    return "TaskIdentifier{" +
        "taskId=" + taskId +
        '}';
  }
}
