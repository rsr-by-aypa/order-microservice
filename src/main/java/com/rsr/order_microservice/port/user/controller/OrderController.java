package com.rsr.order_microservice.port.user.controller;

import com.rsr.order_microservice.port.user.dto.OrderRequestDTO;
import com.rsr.order_microservice.domain.model.Order;
import com.rsr.order_microservice.domain.service.interfaces.OrderRepository;
import com.rsr.order_microservice.domain.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/{userId}")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequestDTO orderRequest, @PathVariable UUID userId) {
        orderRequest.setUserId(userId);
        Order order = orderService.createOrder(orderRequest);
        orderService.sendOrderToShoppingCart(order.getId(), order.getUserId());
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}/{userId}")
    public ResponseEntity<Order> getOrder(@PathVariable UUID orderId, @PathVariable UUID userId) {
        return orderService.getOrder(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
