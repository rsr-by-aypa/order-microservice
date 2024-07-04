package com.rsr.order_microservice.domain.service.interfaces;

import com.rsr.order_microservice.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
