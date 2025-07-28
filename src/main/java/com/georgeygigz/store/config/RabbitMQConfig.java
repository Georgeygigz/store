package com.georgeygigz.store.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String PRODUCT_EVENTS_QUEUE = "product.events.queue";
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);


    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;


    @Bean
    public Queue productEventsQueue() {
        return new Queue(PRODUCT_EVENTS_QUEUE, true);
    }

    @PostConstruct
    public void logRabbitMQConfig() {
        logger.info("📦 RabbitMQ Configuration:");
        logger.info("Host: {}", host);
        logger.info("Port: {}", port);
        logger.info("Username: {}", username);
        logger.info("Password: {}", (password != null && !password.isEmpty() ? "**** (set)" : "NOT SET"));
    }
}
