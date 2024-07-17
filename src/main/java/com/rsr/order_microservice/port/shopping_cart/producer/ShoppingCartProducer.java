package com.rsr.order_microservice.port.shopping_cart.producer;

import com.rsr.order_microservice.port.payment.producer.PaymentProducer;
import com.rsr.order_microservice.port.shopping_cart.dto.OrderCreatedDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartProducer {

    @Value("${rabbitmq.rsr.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.order.shoppingCart.routing_key}")
    private String orderToShoppingCartRoutingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public ShoppingCartProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderCreated(OrderCreatedDto orderCreatedDto) {
        LOGGER.info("Sending payment request message: {}", orderCreatedDto);
        rabbitTemplate.convertAndSend(exchange, orderToShoppingCartRoutingKey, orderCreatedDto);
    }
}
