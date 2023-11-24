package com.distrupify.services;

import com.distrupify.models.ProductModel;
import com.distrupify.repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ProductService {
    @Inject
    ProductRepository productRepository;

    public List<ProductModel> findAll(Long organizationId) {
        return productRepository.findAll(organizationId);
    }

}
