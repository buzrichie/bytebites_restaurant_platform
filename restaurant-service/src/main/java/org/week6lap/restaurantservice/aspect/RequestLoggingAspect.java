package org.week6lap.restaurantservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class RequestLoggingAspect {

    // Pointcut for all methods in controller package
    @Pointcut("execution(* org.week6lap.restaurantservice.controller..*(..))")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.info("[REQUEST] {}.{}() with args: {}",
                signature.getDeclaringType().getSimpleName(),
                signature.getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "response")
    public void logAfterReturning(JoinPoint joinPoint, Object response) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.info("[RESPONSE] {}.{}() returned: {}",
                signature.getDeclaringType().getSimpleName(),
                signature.getName(),
                response instanceof ResponseEntity ? ((ResponseEntity<?>) response).getBody() : response);
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.error("[EXCEPTION] {}.{}() threw: {}",
                signature.getDeclaringType().getSimpleName(),
                signature.getName(),
                ex.getMessage(), ex);
    }
}
