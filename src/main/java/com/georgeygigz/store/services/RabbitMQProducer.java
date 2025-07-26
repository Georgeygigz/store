package com.georgeygigz.store.services;

import com.georgeygigz.store.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendProductEvent(String message){
        rabbitTemplate.convertAndSend(RabbitMQConfig.PRODUCT_EVENTS_QUEUE, message);
    }

}
