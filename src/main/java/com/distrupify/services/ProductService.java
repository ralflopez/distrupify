package com.distrupify.services;

import com.distrupify.entities.ProductEntity;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class ProductService {
    @Inject
    JPAStreamer jpaStreamer;

    public Optional<ProductEntity> findProductById(Long organizationId, Long id) {
        return jpaStreamer.stream(ProductEntity.class)
                .filter(p -> p.getOrganizationId().equals(organizationId))
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }
}
