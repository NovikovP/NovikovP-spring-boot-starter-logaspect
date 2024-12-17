package com.spring.project.aspect.controller;

import com.spring.project.aspect.annotation.LogAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.text.MessageFormat;
import java.util.Arrays;

@Aspect
public class ControllerLogAspect {

    private final String logLevel;

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class.getName());

    public ControllerLogAspect(String logLevel) {
        this.logLevel = logLevel;
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {}

    @Around("controllerMethods()")
    public Object logHttpRequests(ProceedingJoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String requestMessage = MessageFormat.format(
                "Calling from class {0} method {1} with args: {2}",
                className,
                methodName,
                Arrays.toString(args)
        );

        log(requestMessage, logLevel);

        Object response;
        try {
            response = joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("Exception in method: {}", throwable.getMessage());
            throw new RuntimeException("Exception in method: " + throwable.getMessage(), throwable);
        }

        String responseMessage = "HTTP Response: " + response;
        log(responseMessage, logLevel);
        return response;
    }

    private void log(String message, String logLevel) {
        Level level = Level.valueOf(logLevel.toUpperCase());
        switch (level) {
            case TRACE -> logger.trace(message);
            case DEBUG -> logger.debug(message);
            case INFO -> logger.info(message);
            case WARN -> logger.warn(message);
            case ERROR -> logger.error(message);
        }
    }
}
