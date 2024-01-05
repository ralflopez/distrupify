package com.distrupify.dto;

import com.distrupify.entities.InventoryAdjustmentEntity;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

public record InventoryAdjustmentDTO(
    @Nonnull @NotNull Long id,
    @Nonnull @NotNull String createdAt,
    @Nonnull @NotNull InventoryTransactionDTO inventoryTransaction) {
    public static InventoryAdjustmentDTO fromEntity(InventoryAdjustmentEntity entity) {
        return new InventoryAdjustmentDTO(entity.getId(),
                entity.getCreatedAt().toString(),
                InventoryTransactionDTO.fromEntity(entity.getInventoryTransaction()));
    }
}
