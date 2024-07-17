package com.rsr.order_microservice.port.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PaymentRequestDTO {
    private UUID userId;
    private Double amount;
    private String paymentMethod;
}
