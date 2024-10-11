package org.example.tasks.exception;

import java.text.MessageFormat;
import org.example.tasks.constant.ErrorMsg;

public class TaskNotFoundException extends RuntimeException {
  public TaskNotFoundException(String taskId) {
    super(MessageFormat.format(ErrorMsg.TASK_NOT_FOUND, taskId));
  }
}
