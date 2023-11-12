package com.distrupify.product;

import com.distrupify.entities.Product;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class ProductService {
    @Inject
    JPAStreamer jpaStreamer;

    public Optional<Product> findProductById(Long id) {
        return jpaStreamer.stream(Product.class)
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }
}
