package com.rsr.order_microservice.port.user.producer;

import com.rsr.order_microservice.port.user.dto.PaymentRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    @Value("${rabbitmq.rsr.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.order.toPay.routing_key}")
    private String orderToPayRoutingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPaymentRequest(PaymentRequestDTO paymentRequest) {
        LOGGER.info("Sending payment request message: {}", paymentRequest);
        rabbitTemplate.convertAndSend(exchange, orderToPayRoutingKey, paymentRequest);
    }
}