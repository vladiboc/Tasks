package org.example.tasks.web.v1.controller;

import jakarta.validation.ConstraintViolationException;
import java.text.MessageFormat;
import lombok.extern.slf4j.Slf4j;
import org.example.tasks.aop.Loggable;
import org.example.tasks.constant.ErrorMsg;
import org.example.tasks.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

@Loggable
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {
  @ExceptionHandler(UserNotFoundException.class)
  public Mono<ResponseEntity<String>> userNotFound(UserNotFoundException ex) {
    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public Mono<ResponseEntity<String>> constraintViolation(ConstraintViolationException ex) {
    log.error(ErrorMsg.CONSTRAINT_VIOLATION, ex);
    final var errors = ex.getConstraintViolations().stream()
        .map(cv -> MessageFormat
            .format(ErrorMsg.PARAMETER_ERROR, cv.getInvalidValue(), cv.getMessage()))
        .toList();
    final var errorMessage = String.join("; ", errors);
    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage));
  }

  @ExceptionHandler(WebExchangeBindException.class)
  public Mono<ResponseEntity<String>> webExchangeBind(WebExchangeBindException ex) {
    log.error(ErrorMsg.WEB_EXCHANGE_ERROR, ex);
    final var errors = ex.getBindingResult().getAllErrors().stream()
        .map(error -> MessageFormat.format(
            ErrorMsg.FIELD_ERROR, ((FieldError) error).getField(), error.getDefaultMessage()))
        .toList();
    final var errorMessage = String.join("; ", errors);
    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage));
  }

  @ExceptionHandler(Throwable.class)
  public Mono<ResponseEntity<String>> technicalError(Throwable ex) {
    log.error(ErrorMsg.UNDEFINED_SERVER_ERROR, ex);
    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()));
  }
}
