package com.rsr.order_microservice.domain.service.interfaces;

import com.rsr.order_microservice.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
