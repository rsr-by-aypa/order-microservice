package com.rsr.order_microservice.domain.service.impl;

import com.rsr.order_microservice.domain.model.Product;
import com.rsr.order_microservice.domain.service.interfaces.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void updateProduct(Product receivedProduct) {
        Product productToSave = new Product(
                receivedProduct.getProductId(),
                receivedProduct.getPriceInEuro(),
                receivedProduct.getProductName()
        );
        productRepository.save(productToSave);
    }
}
