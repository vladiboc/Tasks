package org.example.tasks.exception;

import java.text.MessageFormat;
import org.example.tasks.constant.ErrorMsg;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String userId) {
    super(MessageFormat.format(ErrorMsg.USER_NOT_FOUND, userId));
  }
}
