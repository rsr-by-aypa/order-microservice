package com.rsr.order_microservice.domain.service.interfaces;


import com.rsr.order_microservice.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
