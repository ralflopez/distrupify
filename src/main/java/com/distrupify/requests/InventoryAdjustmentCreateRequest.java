package com.distrupify.requests;

import com.distrupify.entities.InventoryLogEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
    public static class Item {
        public Long productId;

        @Min(value = 1)
        public Integer quantity;

        public InventoryLogEntity.Type inventoryLogType;
    }

    @Valid
    public List<Item> items;
}
