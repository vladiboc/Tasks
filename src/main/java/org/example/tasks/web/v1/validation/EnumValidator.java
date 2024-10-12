package org.example.tasks.web.v1.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;
import org.example.tasks.aop.Loggable;

@Loggable
public class EnumValidator implements ConstraintValidator<EnumValid, String> {
  private Class<?> enumClass;
  private boolean nullable;

  @Override
  public void initialize(EnumValid annotation) {
    this.enumClass = annotation.enumClass();
    this.nullable = annotation.nullable();
  }

  @Override
  public boolean isValid(String checkedValue, ConstraintValidatorContext context) {
    if (Objects.nonNull(checkedValue) && !checkedValue.isEmpty()) {
      final var enumConstant = Arrays.stream(enumClass.getEnumConstants())
          .filter(c -> c.toString().equals(checkedValue.toUpperCase())).findFirst().orElse(null);
      return !Objects.isNull(enumConstant);
    }
    return this.nullable;
  }
}
