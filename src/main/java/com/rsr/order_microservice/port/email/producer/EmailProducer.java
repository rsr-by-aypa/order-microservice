package com.rsr.order_microservice.port.email.producer;

import com.rsr.order_microservice.port.user.dto.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailProducer {

    @Value("${rabbitmq.rsr.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.order.sendEmail.routing_key}")
    private String orderToEmailRoutingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public EmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrdertoEmail(OrderDTO order) {
        LOGGER.info("Sending order to email {}: {}", order.getEmailAddress(), order);
        rabbitTemplate.convertAndSend(exchange, orderToEmailRoutingKey, order);
    }
}
