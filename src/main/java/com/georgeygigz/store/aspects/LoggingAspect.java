package com.georgeygigz.store.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private static final String TRACE_ID = "traceId";
    @AfterThrowing(
            pointcut = "execution(* com.georgeygigz.store.controllers..*(..)) || " +
                    "execution(* com.georgeygigz.store.services..*(..))",
            throwing = "ex"
    )
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String traceId = MDC.get(TRACE_ID);
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        log.info("TraceId: {}, Class: {}, Entering method {} with parameters {}",
                traceId, className, methodName, joinPoint.getArgs());

        Object result;
        try{
            result = joinPoint.proceed();
        }catch (Throwable throwable){
            log.error("TraceId: {}, Class: {}, Exception in method {}: {}",
                    traceId, className, methodName, throwable.getMessage());
            throw throwable;
        }

        if (result != null) {
            log.info("TraceId: {}, Class: {}, Exiting method {} with return value {}",
                    traceId, className, methodName, result);
        } else {
            log.info("TraceId: {}, Class: {}, Exiting method {} with no return value",
                    traceId, className, methodName);
        }

        return result;

    }

}