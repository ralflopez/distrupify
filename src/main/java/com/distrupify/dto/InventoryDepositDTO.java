package com.distrupify.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

public record InventoryDepositDTO(
        @Nonnull @NotNull Long id,
        @Nonnull @NotNull InventoryTransactionDTO inventoryTransaction
) {
}
