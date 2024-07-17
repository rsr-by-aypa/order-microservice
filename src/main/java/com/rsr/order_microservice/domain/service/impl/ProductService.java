package com.rsr.order_microservice.domain.service.impl;

import com.rsr.order_microservice.domain.model.Product;
import com.rsr.order_microservice.domain.service.interfaces.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void updateProduct(Product receivedProduct) {
        Product productToSave = new Product(
                receivedProduct.getId(),
                receivedProduct.getPriceInEuro(),
                receivedProduct.getName()
        );
        log.info("Persist new Product {}", receivedProduct);
        productRepository.save(productToSave);
    }
}
