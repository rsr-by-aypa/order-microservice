package com.rsr.order_microservice;

import com.rsr.order_microservice.port.user.dto.PaymentRequestDTO;
import com.rsr.order_microservice.port.user.producer.OrderProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderProducer orderProducer;

    @Value("${rabbitmq.rsr.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.order.toPay.routing_key}")
    private String routingKey;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendPaymentRequest() {
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO("user1", 100.0, "Credit Card");

        orderProducer.sendPaymentRequest(paymentRequest);

        ArgumentCaptor<String> exchangeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<PaymentRequestDTO> messageCaptor = ArgumentCaptor.forClass(PaymentRequestDTO.class);

        verify(rabbitTemplate, times(1)).convertAndSend(exchangeCaptor.capture(), routingKeyCaptor.capture(), messageCaptor.capture());

        assertEquals(exchange, exchangeCaptor.getValue());
        assertEquals(routingKey, routingKeyCaptor.getValue());
        assertEquals(paymentRequest, messageCaptor.getValue());
    }
}

