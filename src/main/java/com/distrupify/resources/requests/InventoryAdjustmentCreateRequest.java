package com.distrupify.resources.requests;

import com.distrupify.entities.InventoryLogEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InventoryAdjustmentCreateRequest {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class InventoryAdjustmentCreateRequestItem {
        @NotNull
        public Long productId;

        @Min(value = 1)
        @NotNull
        public Integer quantity;

        @NotNull
        public InventoryLogEntity.InventoryLogType inventoryLogType;
    }

    @Valid
    @NotNull
    public List<InventoryAdjustmentCreateRequestItem> items;
}
