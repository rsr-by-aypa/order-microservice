package com.rsr.order_microservice.port.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.product.created.queue.name}")
    private String productCreatedQueue;

    @Value("${rabbitmq.product.updated.queue.name}")
    private String productUpdatedQueue;

    @Value("${rabbitmq.payment.success.queue.name}")
    private String paymentSuccessQueue;

    @Value("${rabbitmq.rsr.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.created}")
    private String productCreatedBindingKey;

    @Value("${rabbitmq.routing.updated}")
    private String productUpdatedBindingKey;

    @Value("${rabbitmq.payment.success}")
    private String paymentSuccessBindingKey;

    @Value("${rabbitmq.order.toPay.routing_key}")
    private String orderToPayRoutingKey;

    @Bean
    public Queue productCreatedQueue() {
        return new Queue(productCreatedQueue);
    }

    @Bean
    public Queue productUpdatedQueue() {
        return new Queue(productUpdatedQueue);
    }

    @Bean
    public Queue paymentSuccessQueue() {
        return new Queue(paymentSuccessQueue);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding productCreatedBinding() {
        return BindingBuilder.bind(productCreatedQueue()).to(exchange()).with(productCreatedBindingKey);
    }

    @Bean
    public Binding productUpdatedBinding() {
        return BindingBuilder.bind(productUpdatedQueue()).to(exchange()).with(productUpdatedBindingKey);
    }

    @Bean
    public Binding paymentSuccessBinding() {
        return BindingBuilder.bind(paymentSuccessQueue()).to(exchange()).with(paymentSuccessBindingKey);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
