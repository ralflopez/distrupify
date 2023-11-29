package com.distrupify.services;

import com.distrupify.models.ProductModel;
import com.distrupify.repository.ProductRepository;
import com.distrupify.utils.Pageable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductService {
    @Inject
    ProductRepository productRepository;

    public List<ProductModel> findAll(Long organizationId, Pageable pageable) {
        return productRepository.findAll(organizationId, Optional.of(pageable));
    }

    public List<ProductModel> findAll(Long organizationId) {
        return productRepository.findAll(organizationId, Optional.empty());
    }

    public int getPageCount(Long organizationId, int pageSize) {
        final var productCount = productRepository.getProductsCount(organizationId);
        return (int) Math.ceilDiv(productCount, pageSize);
    }

}
