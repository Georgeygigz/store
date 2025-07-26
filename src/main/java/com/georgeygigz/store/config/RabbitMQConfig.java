package com.georgeygigz.store.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String PRODUCT_EVENTS_QUEUE = "product.events.queue";

    @Bean
    public Queue productEventsQueue() {
        return new Queue(PRODUCT_EVENTS_QUEUE, true);
    }
}
