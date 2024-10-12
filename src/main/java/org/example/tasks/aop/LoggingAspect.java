package org.example.tasks.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Аспект логирования.
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {
  @Before("@within(Loggable) || @annotation(Loggable)")
  public void logBefore(final JoinPoint joinPoint) {
    log.info("Вызов метода: {}", joinPoint.getSignature().toShortString());
  }

  @After("@within(Loggable) || @annotation(Loggable)")
  public void logAfter(final JoinPoint joinPoint) {
    log.info("Конец вызова: {}", joinPoint.getSignature().toShortString());
  }

  @AfterReturning(pointcut = "@within(Loggable) || @annotation(Loggable)", returning = "result")
  public void logAfterReturning(final JoinPoint joinPoint, final Object result) {
    log.debug(
        "Возврат из метода {} значение: {}", joinPoint.getSignature().toShortString(), result);
  }

  @AfterThrowing(pointcut = "@within(Loggable) || @annotation(Loggable)", throwing = "exception")
  public void logAfterThrowing(final JoinPoint joinPoint, final Exception exception) {
    log.warn("Исключение в методе: {}: {}",
        joinPoint.getSignature().toShortString(), exception.getMessage());
  }

  @Around("@within(Loggable) || @annotation(Loggable)")
  public Object logAround(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    log.debug("Старт отсчёта: {}", proceedingJoinPoint.getSignature().toShortString());
    final long start = System.currentTimeMillis();

    final Object result = proceedingJoinPoint.proceed();

    final long duration = System.currentTimeMillis() - start;
    log.debug("Продолжительность: {} мс, метод: {}",
        duration, proceedingJoinPoint.getSignature().toShortString());

    return result;
  }
}
