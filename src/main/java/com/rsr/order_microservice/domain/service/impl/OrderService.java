package com.rsr.order_microservice.domain.service.impl;

import com.example.orderservice.dto.OrderRequest;
import com.rsr.order_microservice.domain.model.Order;
import com.rsr.order_microservice.domain.model.Product;
import com.rsr.order_microservice.domain.service.interfaces.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    public Order createOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setFirstName(orderRequest.getFirstName());
        order.setLastName(orderRequest.getLastName());
        order.setEmail(orderRequest.getEmail());
        order.setAddress(orderRequest.getAddress());
        order.setPaymentInfo(orderRequest.getPaymentInfo());

        List<Product> products = orderRequest.getBoughtProducts().stream()
                .map(productRequest -> new Product(
                        null,
                        productRequest.getProductId(),
                        productRequest.getPrice(),
                        productRequest.getSort(),
                        productRequest.getQuantity()
                )).collect(Collectors.toList());

        order.setProducts(products);

        // Speichere die Bestellung in der Datenbank
        Order savedOrder = orderRepository.save(order);

        // Sende die Bestelldaten an RabbitMQ
        rabbitTemplate.convertAndSend(rabbitMQConfig.ORDER_QUEUE, savedOrder);

        return savedOrder;
    }
}
