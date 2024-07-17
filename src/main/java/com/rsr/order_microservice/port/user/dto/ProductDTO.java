package com.rsr.order_microservice.port.user.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductDTO {

    private UUID productId;

    private String name;

    private int amount;

    private double priceInEuro;
}