package org.example.tasks.web.v1.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.example.tasks.constant.ErrorMsg;


@Documented
@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValid {
  Class<?> enumClass();
  boolean nullable() default false;
  String message() default ErrorMsg.WRONG_ENUM;
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
