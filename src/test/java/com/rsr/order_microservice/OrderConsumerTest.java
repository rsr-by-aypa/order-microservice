package com.rsr.order_microservice;

import com.rsr.order_microservice.domain.model.Product;
import com.rsr.order_microservice.domain.service.impl.OrderService;
import com.rsr.order_microservice.port.user.consumer.OrderConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class OrderConsumerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderConsumer orderConsumer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleProductCreated() {
        Product product = new Product(1L, 1L, 10.0, "Opal", 1);
        orderConsumer.handleProductCreated(product);
        verify(orderService, times(1)).updateProduct(product);
    }

    @Test
    public void testHandleProductUpdated() {
        Product product = new Product(1L, 1L, 10.0, "Crystal", 1);
        orderConsumer.handleProductUpdated(product);
        verify(orderService, times(1)).updateProduct(product);
    }


    @Test
    public void testHandlePaymentSuccess() {
        Long orderId = 1L;
        orderConsumer.handlePaymentSuccess(orderId);
        verify(orderService, times(1)).completePayment(orderId);
    }
}