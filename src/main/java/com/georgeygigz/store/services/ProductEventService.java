package com.georgeygigz.store.services;

import com.georgeygigz.store.entities.Product;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductEventService {
    private final RabbitMQProducer rabbitMQProducer;

    @Async
    public void sendProductCreatedEvent(Product product) {
        // TODO: There must be an elegant way to build the event message
        rabbitMQProducer.sendProductEvent("Product created: " + product.getName() + " (ID: " + product.getId() + ")");
    }

    @Async
    public void sendProductUpdatedEvent(Product product) {
        rabbitMQProducer.sendProductEvent("Product updated: " + product.getName() + " (ID: " + product.getId() + ")");
    }

    @Async
    public void sendProductDeletedEvent(Long productId) {
        rabbitMQProducer.sendProductEvent("Product deleted: ID " + productId);
    }
}
