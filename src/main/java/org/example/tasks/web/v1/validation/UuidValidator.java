package org.example.tasks.web.v1.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.example.tasks.aop.Loggable;

@Loggable
public class UuidValidator implements ConstraintValidator<UuidValid, String> {
  private static final String UUID =
      "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
  private boolean nullable;

  @Override
  public void initialize(UuidValid annotation) {
      this.nullable = annotation.nullable();
    }

  @Override
  public boolean isValid(String checkedValue, ConstraintValidatorContext context) {
    if (Objects.nonNull(checkedValue)) {
      return checkedValue.trim().matches(UUID);
    }
    return this.nullable;
  }
}
