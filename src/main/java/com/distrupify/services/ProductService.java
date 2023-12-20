package com.distrupify.services;

import com.distrupify.models.ProductModel;
import com.distrupify.repository.ProductRepository;
import com.distrupify.requests.ProductCreateRequest;
import com.distrupify.requests.ProductEditRequest;
import com.distrupify.requests.ProductSearchFilterBy;
import com.distrupify.utils.Pageable;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ProductService {
    @Inject
    ProductRepository productRepository;

    public List<ProductModel> findAll(@Nonnull Long organizationId) {
        return productRepository.findAll(organizationId, Pageable.all());
    }

    public List<ProductModel> findAll(@Nonnull Long organizationId, @Nonnull Pageable pageable) {
        return productRepository.findAll(organizationId, pageable);
    }

    public List<ProductModel> findAll(@Nonnull Long organizationId, @Nonnull Pageable pageable,
                                      @Nonnull String searchString, @Nonnull ProductSearchFilterBy filterBy) {
        return productRepository.findAll(organizationId, pageable, searchString, filterBy);
    }

    public int getPageCount(@Nonnull Long organizationId, int pageSize) {
        final var productCount = productRepository.getCount(organizationId);
        return (int) Math.ceilDiv(productCount, pageSize);
    }

    public void create(@Nonnull Long organizationId, @Nonnull ProductCreateRequest request) {
        productRepository.create(organizationId, request);
    }

    public void softDelete(@Nonnull Long organizationId, @Nonnull Long productId) {
        productRepository.softDelete(organizationId, productId);
    }

    public void edit(@Nonnull Long organizationId, @Nonnull Long productId, @Nonnull ProductEditRequest request) {
        productRepository.edit(organizationId, productId, request);
    }

}
