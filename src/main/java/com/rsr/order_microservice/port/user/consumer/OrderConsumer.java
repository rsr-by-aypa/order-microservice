package com.rsr.order_microservice.port.user.consumer;

import com.rsr.order_microservice.domain.model.Product;
import com.rsr.order_microservice.domain.service.impl.OrderService;
import com.rsr.order_microservice.domain.service.impl.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @RabbitListener(queues = "${rabbitmq.product.created.queue.name}")
    public void handleProductCreated(Product product) {
        LOGGER.info("Received product created message: {}", product);
        productService.updateProduct(product);
    }

    @RabbitListener(queues = "${rabbitmq.product.updated.queue.name}")
    public void handleProductUpdated(Product product) {
        LOGGER.info("Received product updated message: {}", product);
        productService.updateProduct(product);
    }

    @RabbitListener(queues = "${rabbitmq.payment.success.queue.name}")
    public void handlePaymentSuccess(UUID orderId) {
        LOGGER.info("Received payment success message for order ID: {}", orderId);
        orderService.completePayment(orderId);
    }
}
