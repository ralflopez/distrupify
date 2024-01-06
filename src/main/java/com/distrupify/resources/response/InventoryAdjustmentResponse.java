package com.distrupify.resources.response;

import com.distrupify.resources.dto.InventoryAdjustmentDTO;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record InventoryAdjustmentResponse(@Nonnull @NotNull List<InventoryAdjustmentDTO> inventoryAdjustments,
                                          @Nonnull @NotNull Integer pageCount
) {
}
