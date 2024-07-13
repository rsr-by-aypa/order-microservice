package com.rsr.order_microservice;

import com.rsr.order_microservice.domain.model.Order;
import com.rsr.order_microservice.domain.model.Product;
import com.rsr.order_microservice.domain.service.interfaces.OrderRepository;
import com.rsr.order_microservice.domain.service.impl.OrderService;
import com.rsr.order_microservice.domain.service.interfaces.ProductRepository;
import com.rsr.order_microservice.port.user.dto.OrderRequestDTO;
import com.rsr.order_microservice.port.user.dto.PaymentRequestDTO;
import com.rsr.order_microservice.port.user.producer.OrderProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderProducer orderProducer;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder() {
        OrderRequestDTO orderRequest = new OrderRequestDTO();
        orderRequest.setUserId("user1");
        orderRequest.setFirstName("John");
        orderRequest.setLastName("Doe");
        orderRequest.setEmail("john.doe@example.com");
        orderRequest.setAddress("123 Street");
        orderRequest.setPaymentInfo("Credit Card");

        OrderRequestDTO.ProductRequest productRequest = new OrderRequestDTO.ProductRequest();
        productRequest.setProductId(1L);
        productRequest.setPrice(10.0);
        productRequest.setSort("Opal");
        productRequest.setQuantity(1);
        orderRequest.setBoughtProducts(Arrays.asList(productRequest));

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setUserId("user1");
        savedOrder.setFirstName("John");
        savedOrder.setLastName("Doe");
        savedOrder.setEmail("john.doe@example.com");
        savedOrder.setAddress("123 Street");
        savedOrder.setPaymentInfo("Credit Card");
        savedOrder.setProducts(Arrays.asList(new Product(1L, 1L, 10.0, "Electronics", 1)));

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order createdOrder = orderService.createOrder(orderRequest);

        assertNotNull(createdOrder);
        assertEquals("user1", createdOrder.getUserId());
        assertEquals("123 Street", createdOrder.getAddress());

        ArgumentCaptor<PaymentRequestDTO> paymentRequestCaptor = ArgumentCaptor.forClass(PaymentRequestDTO.class);
        verify(orderProducer, times(1)).sendPaymentRequest(paymentRequestCaptor.capture());
        PaymentRequestDTO capturedPaymentRequest = paymentRequestCaptor.getValue();
        assertEquals("user1", capturedPaymentRequest.getUserId());
        assertEquals(10.0, capturedPaymentRequest.getAmount());
    }


    @Test
    public void testUpdateProduct() {
        Product product = new Product(1L, 1L, 20.0, "Opal", 1);
        orderService.updateProduct(product);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testCompletePayment() {
        Order order = new Order();
        order.setId(1L);
        order.setPaymentCompleted(false);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.completePayment(1L);

        assertTrue(order.isPaymentCompleted());
        verify(orderRepository, times(1)).save(order);
    }
}
