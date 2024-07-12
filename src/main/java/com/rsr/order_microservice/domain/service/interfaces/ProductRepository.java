package com.rsr.order_microservice.domain.service.interfaces;


import com.rsr.order_microservice.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
