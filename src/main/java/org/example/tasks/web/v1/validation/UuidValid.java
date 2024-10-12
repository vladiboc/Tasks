package org.example.tasks.web.v1.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.example.tasks.constant.ErrorMsg;

/**
 * Аннотация для валидации строки на соответсвие регулярному выражению UUID.
 */
@Documented
@Constraint(validatedBy = UuidValidator.class)
@Target({ElementType.TYPE_USE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface UuidValid {
  String message() default ErrorMsg.WRONG_ID;
  boolean nullable() default false;
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
