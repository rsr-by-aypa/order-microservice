package com.rsr.order_microservice.domain.service.impl;

import com.rsr.order_microservice.domain.model.Item;
import com.rsr.order_microservice.domain.model.Order;
import com.rsr.order_microservice.domain.model.Product;
import com.rsr.order_microservice.domain.service.interfaces.OrderRepository;
import com.rsr.order_microservice.domain.service.interfaces.ProductRepository;
import com.rsr.order_microservice.port.shopping_cart.dto.OrderCreatedDto;
import com.rsr.order_microservice.port.shopping_cart.producer.ShoppingCartProducer;
import com.rsr.order_microservice.port.user.dto.OrderDTO;
import com.rsr.order_microservice.port.user.dto.OrderRequestDTO;
import com.rsr.order_microservice.port.user.dto.PaymentRequestDTO;
import com.rsr.order_microservice.port.email.producer.EmailProducer;
import com.rsr.order_microservice.port.payment.producer.PaymentProducer;
import com.rsr.order_microservice.utils.exceptions.UnknownProductIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Autowired
    private ShoppingCartProducer shoppingCartProducer;

    public Order createOrder(OrderRequestDTO orderRequest) {

        Order order = convertDTOToOrder(orderRequest);
        order.setTotalPrice(order.getTotalPrice());

        Order savedOrder = orderRepository.save(order);

        // Sende die Zahlungsanfrage an RabbitMQ
        sendPaymentRequest(savedOrder);
        //sende Email mit Daten
        sendOrderToEmail(savedOrder);

        return savedOrder;
    }

    private Order convertDTOToOrder(OrderRequestDTO orderRequest) {
        List<Item> items = extractItemsFromOrderRequest(orderRequest);

        Order order = new Order(
                UUID.randomUUID(),
                orderRequest.getUserId(),
                orderRequest.getFirstName(),
                orderRequest.getLastName(),
                orderRequest.getEmail(),
                orderRequest.getAddress(),
                orderRequest.getPaymentInfo(),
                false,
                LocalDateTime.now(),
                items,
                0
        );

        return order;
    }

    private List<Item> extractItemsFromOrderRequest(OrderRequestDTO orderRequest) {
        List<Item> items = orderRequest.getBoughtItems().stream()
                .map(itemRequest -> {
                    // Produkt anhand der ID in der Datenbank suchen
                    Product product = productRepository.findById(itemRequest.getProductId())
                            .orElseThrow(() -> new UnknownProductIdException());

                    // Produktinformationen in ein Item-Objekt übertragen
                    return new Item(
                            product.getId(),
                            product.getName(),
                            product.getPriceInEuro(),
                            itemRequest.getQuantity()
                    );
                }).toList();

        return items;
    }

    private void sendPaymentRequest(Order order) {
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO(order.getUserId(), order.getTotalPrice(), order.getPaymentInfo());
        paymentProducer.sendPaymentRequest(paymentRequest);
    }

    private void sendOrderToEmail(Order order) {
        OrderDTO orderToSend = new OrderDTO(order.getId(), order.getUserId(), order.getFirstName(), order.getLastName(), LocalDateTime.now(), order.getEmail(), order.getItems());
        emailProducer.sendOrdertoEmail(orderToSend);
    }

    public void completePayment(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));
        order.setPaymentCompleted(true);
        orderRepository.save(order);
    }

    public void sendOrderToShoppingCart(UUID orderId, UUID userId) {
        OrderCreatedDto orderCreatedDto = new OrderCreatedDto(orderId, userId);
        shoppingCartProducer.sendOrderCreated(orderCreatedDto);
    }

    public Optional<Order> getOrder(UUID id) {
        return orderRepository.findById(id);
    }
}
