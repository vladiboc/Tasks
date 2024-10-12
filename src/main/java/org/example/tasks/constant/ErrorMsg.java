package org.example.tasks.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMsg {
  public static final String USER_NOT_FOUND = "Не найден пользователь с id: {0}!";
  public static final String TASK_NOT_FOUND = "Не найденa задача с id: {0}!";
  public static final String WRONG_ID = "Некорректный формат UUID!";
  public static final String UNDEFINED_SERVER_ERROR = "Неопределенная ошибка сервера:";
  public static final String CONSTRAINT_VIOLATION = "Нарушение ограничений для параметра!";
  public static final String PARAMETER_ERROR = "Значение: {0} Ошибка: {1}";
  public static final String WEB_EXCHANGE_ERROR = "Ошибка Веб-обмена!";
  public static final String FIELD_ERROR = "Поле: {0} Ошибка: {1}";
  public static final String CANNOT_BE_BLANK = "Не может быть пустым!";
  public static final String WRONG_ENUM = "Некорректное занчение перечисления!";
}
