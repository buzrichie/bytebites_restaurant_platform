package org.week6lap.restaurantservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExceptionLoggingAspect {

    /**
     * Pointcut to intercept all @ExceptionHandler methods in the GlobalExceptionHandler class
     */
    @Pointcut("execution(* org.week6lap.restaurantservice.exception.GlobalExceptionHandler.*(..))")
    public void globalExceptionHandlers() {}

    /**
     * Log after each exception handler returns a ResponseEntity
     */
    @AfterReturning(pointcut = "globalExceptionHandlers()", returning = "response")
    public void logHandledExceptions(JoinPoint joinPoint, Object response) {
        if (response instanceof ResponseEntity<?> entity) {
            log.error(
                    "\n [Handled Exception] Method: {}\n Status: {}\n Response Body: {}\n",
                    joinPoint.getSignature().getName(),
                    entity.getStatusCode(),
                    entity.getBody()
            );
        }
    }

    /**
     * Optionally log unhandled errors thrown from exception handlers
     */
    @AfterThrowing(pointcut = "globalExceptionHandlers()", throwing = "ex")
    public void logUnhandledException(JoinPoint joinPoint, Throwable ex) {
        log.error(
                "\n [Unhandled Exception in Handler] Method: {}\n Exception: {}\n Message: {}\n",
                joinPoint.getSignature().getName(),
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
    }
}
