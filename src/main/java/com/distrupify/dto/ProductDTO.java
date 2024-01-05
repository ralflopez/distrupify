package com.distrupify.dto;

import com.distrupify.entities.ProductEntity;
import com.distrupify.models.ProductModel;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

public record ProductDTO(
        @Nonnull @NotNull Long id,
        @Nonnull @NotNull String sku,
        @Nonnull @NotNull String brand,
        @Nonnull @NotNull String name,
        @Nonnull @NotNull String description,
        @Nonnull @NotNull Double unitPrice,
        Integer quantity
) {

    public static ProductDTO fromModel(ProductModel model) {
        return new ProductDTO(model.id().orElse(-1L),
                model.sku(),
                model.brand(),
                model.name(),
                model.description(),
                model.unitPrice(),
                model.quantity());
    }
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
