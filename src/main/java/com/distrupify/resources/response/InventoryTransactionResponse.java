package com.distrupify.resources.response;

import com.distrupify.resources.dto.InventoryTransactionDTO;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record InventoryTransactionResponse(@Nonnull @NotNull List<InventoryTransactionDTO> transactions,
                                           @Nonnull @NotNull Integer pageCount
) {
}
