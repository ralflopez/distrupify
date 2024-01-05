package com.distrupify.response;

import com.distrupify.dto.InventoryAdjustmentDTO;
import com.distrupify.dto.InventoryTransactionDTO;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record InventoryTransactionResponse(@Nonnull @NotNull List<InventoryTransactionDTO> transactions,
                                           @Nonnull @NotNull Integer pageCount
) {
}
