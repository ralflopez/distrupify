package com.distrupify.resources.dto;

import com.distrupify.entities.InventoryLogEntity;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

public record InventoryLogDTO (
        @Nonnull @NotNull Long id,
        @Nonnull @NotNull Integer quantity,
        @Nonnull @NotNull Double price,
        @Nonnull @NotNull String inventoryLogType,
        @Nonnull @NotNull String timestamp,
        @Nonnull @NotNull ProductDTO product) {
    public static InventoryLogDTO fromEntity(InventoryLogEntity entity) {
        return new InventoryLogDTO(entity.getId(),
                entity.getQuantity(),
                entity.getUnitPrice().doubleValue(),
                entity.getInventoryLogType().name(),
                entity.getTimestamp().toString(),
                ProductDTO.fromEntity(entity.getProduct()));
    }
}
