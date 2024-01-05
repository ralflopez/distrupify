package com.distrupify.dto;

import com.distrupify.entities.InventoryLogEntity;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

public record TransactionItemDTO(
        @Nonnull @NotNull Integer quantity,
        @Nonnull @NotNull Double price,
        @Nonnull @NotNull InventoryLogEntity.InventoryLogType inventoryLogType,
        @Nonnull @NotNull ProductDTO product
) {
    public static TransactionItemDTO fromEntity(InventoryLogEntity entity) {
        return new TransactionItemDTO(entity.getQuantity(),
                entity.getUnitPrice().doubleValue(),
                entity.getInventoryLogType(),
                ProductDTO.fromEntity(entity.getProduct()));
    }
}
