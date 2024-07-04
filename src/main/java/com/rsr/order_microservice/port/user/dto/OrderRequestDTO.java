package com.rsr.order_microservice.port.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String paymentInfo;
    private List<ProductRequest> boughtProducts;

    @Data
    public static class ProductRequest {
        private Long productId;
        private Double price;
        private String sort;
        private Integer quantity;
    }
}
