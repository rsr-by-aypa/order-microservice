package com.rsr.order_microservice.domain.service.impl;

import com.rsr.order_microservice.domain.model.Order;
import com.rsr.order_microservice.domain.model.Product;
import com.rsr.order_microservice.domain.service.interfaces.OrderRepository;
import com.rsr.order_microservice.domain.service.interfaces.ProductRepository;
import com.rsr.order_microservice.port.user.dto.OrderDTO;
import com.rsr.order_microservice.port.user.dto.OrderRequestDTO;
import com.rsr.order_microservice.port.user.dto.PaymentRequestDTO;
import com.rsr.order_microservice.port.user.producer.EmailProducer;
import com.rsr.order_microservice.port.user.producer.PaymentProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentProducer paymentProducer;

    @Autowired
    private EmailProducer emailProducer;

    public Order createOrder(OrderRequestDTO orderRequest) {
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
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
        order.setPaymentCompleted(false);
        order.setOrderCreationTime(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        // Sende die Zahlungsanfrage an RabbitMQ
        sendPaymentRequest(savedOrder);

        return savedOrder;
    }

    private void sendPaymentRequest(Order order) {
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO(order.getUserId(), order.getTotalPrice(), order.getPaymentInfo());
        paymentProducer.sendPaymentRequest(paymentRequest);
    }

    private void sendOrderToEmail(Order order) {
        OrderDTO orderToSend = new OrderDTO(order.getId(), order.getUserId(), order.getFirstName(), order.getLastName(), LocalDateTime.now() , order.getEmail(), order.getProducts());
        emailProducer.sendOrdertoEmail(orderToSend);
    }

    public void updateProduct(Product product) {
        productRepository.save(product);
    }

    public void completePayment(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));
        order.setPaymentCompleted(true);
        orderRepository.save(order);
    }

    public Optional<Order> getOrder(UUID id) {
        return orderRepository.findById(id);
    }
}
