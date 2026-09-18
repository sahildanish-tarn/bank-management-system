package com.jsp.bank_management_system.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around( "execution(* com.jsp.bank_management_system.controller..*(..))" +
            " || execution(* com.jsp.bank_management_system.service..*(..))")
    public Object logControllerMethods(
            ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName =
                joinPoint.getSignature().toShortString();

        long startTime = System.currentTimeMillis();

        log.info("STARTED : {}", methodName);

        try {

            Object result = joinPoint.proceed();

            long endTime = System.currentTimeMillis();

            log.info(
                    "COMPLETED : {} | Time Taken: {} ms",
                    methodName,
                    endTime - startTime
            );

            return result;

        } catch (Exception e) {

            log.error(
                    "FAILED : {} | Message: {}",
                    methodName,
                    e.getMessage()
            );

            throw e;
        }
    }
}