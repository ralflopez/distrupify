package com.distrupify.requests;

import com.distrupify.models.InventoryLogModel;
import com.distrupify.models.InventoryTransactionModel;
import com.distrupify.validations.ExistingProductId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDepositCreateRequest {
    @Builder
    public static class Item {
        @ExistingProductId(token = true)
        public Long productId;

        @Min(value = 1)
        public Integer quantity;

        @Min(value = 0)
        public Double price;
    }

    @Valid
    public List<Item> items;

    public InventoryTransactionModel<InventoryTransactionModel.Type.InventoryDeposit> intoModel(Long organizationId) {
        final var transaction = InventoryTransactionModel.createInventoryDeposit(organizationId);
        items.forEach(i -> transaction.addLog(InventoryLogModel.Type.INCOMING,
                        i.quantity,
                        i.price,
                        i.productId));
        return transaction;
    }
}
