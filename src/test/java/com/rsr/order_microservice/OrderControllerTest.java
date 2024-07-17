package com.rsr.order_microservice;

import com.rsr.order_microservice.domain.model.Order;
import com.rsr.order_microservice.domain.model.Product;
import com.rsr.order_microservice.domain.service.impl.OrderService;
import com.rsr.order_microservice.port.user.controller.OrderController;
import com.rsr.order_microservice.port.user.dto.OrderRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    UUID userID = UUID.randomUUID();
    UUID productID = UUID.randomUUID();
    UUID orderID = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder() {
        OrderRequestDTO orderRequest = new OrderRequestDTO();
        orderRequest.setUserId(userID);
        orderRequest.setFirstName("John");
        orderRequest.setLastName("Doe");
        orderRequest.setEmail("john.doe@example.com");
        orderRequest.setAddress("123 Street");
        orderRequest.setPaymentInfo("Credit Card");

        OrderRequestDTO.ProductRequest productRequest = new OrderRequestDTO.ProductRequest();
        productRequest.setProductId(productID);
        productRequest.setPrice(10.0);
        productRequest.setSort("Electronics");
        productRequest.setQuantity(1);
        orderRequest.setBoughtProducts(Arrays.asList(productRequest));

        Product product = new Product(productID, productID, 10.0, "Opal", 1);
        Order savedOrder = new Order();
        savedOrder.setId(orderID);
        savedOrder.setUserId(userID);
        savedOrder.setFirstName("John");
        savedOrder.setLastName("Doe");
        savedOrder.setEmail("john.doe@example.com");
        savedOrder.setAddress("123 Street");
        savedOrder.setProducts(Arrays.asList(product));

        when(orderService.createOrder(any(OrderRequestDTO.class))).thenReturn(savedOrder);

        ResponseEntity<Order> response = orderController.createOrder(orderRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(userID, response.getBody().getUserId());
        assertNotNull(response.getBody().getProducts());
        assertEquals(1, response.getBody().getProducts().size());
        assertEquals("Opal", response.getBody().getProducts().get(0).getSort());
    }

    @Test
    public void testGetOrder() {
        Order order = new Order();
        order.setId(orderID);
        order.setUserId(userID);
        order.setFirstName("John");
        order.setLastName("Doe");
        order.setEmail("john.doe@example.com");
        order.setAddress("123 Street");

        when(orderService.getOrder(orderID)).thenReturn(Optional.of(order));

        ResponseEntity<Order> response = orderController.getOrder(orderID);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(userID, response.getBody().getUserId());
    }

    @Test
    public void testGetOrder_NotFound() {
        when(orderService.getOrder(orderID)).thenReturn(Optional.empty());

        ResponseEntity<Order> response = orderController.getOrder(orderID);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}

