package com.spring.project.aspect.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Aspect
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class.getName());

    @Before("@annotation(com.spring.project.aspect.annotation.LogBefore)")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Before invoked method: from class {}: method {} with args: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Arrays.stream(joinPoint.getArgs()).map(Object::toString).toList()
        );
    }

    @AfterThrowing(value = "@annotation(com.spring.project.aspect.annotation.LogThrowing)", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.info("Throw exception from class {}: method {} with message: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                exception.getMessage()
        );
    }

    @AfterReturning(value = "@annotation(com.spring.project.aspect.annotation.LogResult)", returning = "taskResult")
    public void logAfterReturningTask(JoinPoint joinPoint, Object taskResult) {
        logger.info("Call from class {} method {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName()
        );
        logger.info("Method result: {}", taskResult);
    }

    @Around("@annotation(com.spring.project.aspect.annotation.LogSpendTime)")
    public Object around(ProceedingJoinPoint joinPoint) {
        logger.info("Calling from class {} method {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName()
        );

        Object[] args = joinPoint.getArgs();
        logger.info("Method args: {}", Arrays.toString(args));

        Object result;
        long startTime = System.currentTimeMillis();

        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("Exception in method: {}", throwable.getMessage());
            throw new RuntimeException("Exception in method: " + throwable.getMessage(), throwable);
        }

        long endTime = System.currentTimeMillis();
        logger.info("Method result: {}", result);
        logger.info("Method execution time: {} ms", endTime - startTime);

        return result;
    }
}
