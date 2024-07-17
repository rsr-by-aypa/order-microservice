package com.rsr.order_microservice.domain.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID productId;

    private String productName;

    private double priceInEuro;

    private int amount;

    public Item(UUID productId, String productName, double priceInEuro, int amount) {
        this.id = UUID.randomUUID();
        this.productId = productId;
        this.productName = productName;
        this.priceInEuro = priceInEuro;
        this.amount = amount;
    }

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

}

