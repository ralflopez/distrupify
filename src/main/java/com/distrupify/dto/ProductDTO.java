package com.distrupify.dto;

import com.distrupify.entities.ProductEntity;
import jakarta.annotation.Nonnull;

public record ProductDTO(
        long id,
        @Nonnull String sku,
        @Nonnull String brand,
        @Nonnull String name,
        @Nonnull String description,
        double unitPrice,
        Integer quantity
) {
    public static ProductDTO fromEntity(ProductEntity entity) {
        return new ProductDTO(entity.getId(),
                entity.getSku(),
                entity.getBrand(),
                entity.getName(),
                entity.getDescription(),
                entity.getUnitPrice().doubleValue(),
                null);
    }
}
