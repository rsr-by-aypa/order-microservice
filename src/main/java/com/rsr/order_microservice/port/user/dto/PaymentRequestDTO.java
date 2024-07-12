package com.rsr.order_microservice.port.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRequestDTO {
    private String userId;
    private Double amount;
    private String paymentMethod;
}
