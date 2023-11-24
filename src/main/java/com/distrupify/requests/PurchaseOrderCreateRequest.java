package com.distrupify.requests;

import com.distrupify.models.InventoryLogModel;
import com.distrupify.models.InventoryTransactionModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderCreateRequest {
    @Builder
    public static class Item {
        public Long productId;

        @Min(value = 1)
        public Integer quantity;

        @Min(value = 0)
        public Double unitPrice;
    }

    @Valid
    public List<Item> items;

    public InventoryTransactionModel<InventoryTransactionModel.Type.PurchaseOrder> intoModel(Long organizationId) {
        final var transaction = InventoryTransactionModel.createPurchaseOrder(organizationId);
        items.forEach(i -> transaction.addLog(InventoryLogModel.Type.INCOMING,
                        i.quantity,
                        i.unitPrice,
                        i.productId));
        return transaction;
    }
}
