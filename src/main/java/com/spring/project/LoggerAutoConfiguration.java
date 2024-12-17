package com.spring.project;

import com.spring.project.aspect.annotation.LogAspect;
import com.spring.project.aspect.controller.ControllerLogAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LoggerProperties.class)
public class LoggerAutoConfiguration {

    private final LoggerProperties properties;

    public LoggerAutoConfiguration(LoggerProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnProperty(name = "log.annotation.enable", havingValue = "true", matchIfMissing = true)
    public LogAspect logAspect() {
        return new LogAspect();
    }

    @Bean
    @ConditionalOnProperty(name = "log.controller.enable", havingValue = "true", matchIfMissing = true)
    public ControllerLogAspect controllerLogAspect() {
        String logLevel = properties.getController().getLogLevel();
        if (logLevel == null || logLevel.isEmpty()) {
            properties.getController().setLogLevel("INFO");
        }
        return new ControllerLogAspect(properties.getController().getLogLevel());
    }
}
