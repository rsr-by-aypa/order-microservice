package com.rsr.order_microservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rsr_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String paymentInfo;
    private boolean paymentCompleted;
    private LocalDateTime orderCreationTime;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> items;
    private double totalPrice;

    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(item -> item.getPriceInEuro() * item.getAmount())
                .sum();
    }
}
