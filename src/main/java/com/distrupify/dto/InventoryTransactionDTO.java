package com.distrupify.dto;

import com.distrupify.entities.InventoryTransactionEntity;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record InventoryTransactionDTO(
        @Nonnull @NotNull Long id,
        @Nonnull @NotNull InventoryTransactionEntity.InventoryTransactionType inventoryTransactionType,
        @Nonnull @NotNull String timestamp,
        @Nonnull @NotNull List<TransactionItemDTO> items,
        @Nonnull @NotNull InventoryTransactionEntity.InventoryTransactionStatus status) {
    public static InventoryTransactionDTO fromEntity(InventoryTransactionEntity entity) {
        return new InventoryTransactionDTO(entity.getId(),
                entity.getInventoryTransactionType(),
                entity.getTimestamp().toString(),
                entity.getInventoryLogs()
                        .stream()
                        .map(TransactionItemDTO::fromEntity).toList(),
                entity.getStatus());
    }
}