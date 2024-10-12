package org.example.tasks.exception;

import java.text.MessageFormat;
import org.example.tasks.constant.ErrorMsg;

/**
 * Исключение в случае отсутствия в БД сущности Task.
 */
public class TaskNotFoundException extends RuntimeException {
  public TaskNotFoundException(String taskId) {
    super(MessageFormat.format(ErrorMsg.TASK_NOT_FOUND, taskId));
  }
}
