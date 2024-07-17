package com.rsr.order_microservice.port.shopping_cart.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class OrderCreatedDto {

    private UUID orderId;

    private UUID userId;
}
