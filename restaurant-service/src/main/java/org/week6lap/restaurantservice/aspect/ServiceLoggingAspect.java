package org.week6lap.restaurantservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ServiceLoggingAspect {

    // Pointcut for all methods in RestaurantServiceImpl and MenuItemServiceImpl
    @Pointcut("execution(* org.week6lap.restaurantservice.service.impl.RestaurantServiceImpl.*(..)) || " +
            "execution(* org.week6lap.restaurantservice.service.impl.MenuItemServiceImpl.*(..))")
    public void serviceMethods() {}

    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Entering method: {} with arguments: {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Method completed: {} with result: {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error("Exception in method: {} with message: {}", joinPoint.getSignature(), ex.getMessage(), ex);
    }
}
