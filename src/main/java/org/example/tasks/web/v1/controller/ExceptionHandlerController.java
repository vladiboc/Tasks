package org.example.tasks.web.v1.controller;

import org.example.tasks.aop.Loggable;
import org.example.tasks.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Loggable
@RestControllerAdvice
public class ExceptionHandlerController {
  @ExceptionHandler(UserNotFoundException.class)
  public Mono<ResponseEntity<String>> userNotFound(UserNotFoundException ex) {
    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()));
  }
}
