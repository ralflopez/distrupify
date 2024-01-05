package com.distrupify.response;

import com.distrupify.dto.InventoryAdjustmentDTO;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record InventoryAdjustmentResponse(@Nonnull @NotNull List<InventoryAdjustmentDTO> inventoryAdjustments,
                                          @Nonnull @NotNull Integer pageCount
) {
}
