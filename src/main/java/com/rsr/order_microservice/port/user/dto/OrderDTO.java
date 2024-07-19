package com.rsr.order_microservice.port.user.dto;

import com.rsr.order_microservice.domain.model.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OrderDTO {

    private UUID orderId;

    private UUID userId;

    private String firstName;

    private String lastName;

    private LocalDateTime orderCreationTime;

    private String emailAddress;

    private List<Item> items;
}