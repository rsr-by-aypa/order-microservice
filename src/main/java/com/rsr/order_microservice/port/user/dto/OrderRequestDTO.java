package com.rsr.order_microservice.port.user.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderRequestDTO {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String paymentInfo;
    private List<ItemRequest> boughtItems;

    @Data
    public static class ItemRequest {
        private UUID productId;
        private Integer quantity;
    }
}
