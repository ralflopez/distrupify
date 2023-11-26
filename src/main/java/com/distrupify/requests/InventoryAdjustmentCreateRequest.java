package com.distrupify.requests;

import com.distrupify.entities.InventoryLogEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryAdjustmentCreateRequest {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        public Long productId;

        @Min(value = 1)
        public Integer quantity;

        public InventoryLogEntity.Type inventoryLogType;
    }

    @Valid
    public List<Item> items;

//    public InventoryTransactionModel<InventoryTransactionModel.Type.InventoryAdjustment> intoModel(Long organizationId) {
//        final var transaction = InventoryTransactionModel.createInventoryAdjustment(organizationId);
//        items.forEach(i -> transaction.addLog(,
//                        i.quantity,
//                        i.unitPrice,
//                        i.productId));
//        return transaction;
//    }
}
