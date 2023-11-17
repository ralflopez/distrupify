package com.distrupify.models;

import com.distrupify.requests.InventoryDepositCreateRequest;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Use static constructors to construct InventoryTransactionModel
 */
public record InventoryTransactionModel<T extends InventoryTransactionModel.Type>(
        @Nonnull Optional<Long> id,
        @Nonnull Optional<Date> timestamp,
        long organizationId,
        @Nonnull Type details,
        @Nonnull List<InventoryLogModel> inventoryLogs
) {

    public sealed interface Type {
        record PurchaseOrder(Date receivedAt) implements Type {
        }

        record InventoryDeposit() implements Type {
        }
    }

    public static InventoryTransactionModel<Type.PurchaseOrder> createPurchaseOrder(Long organizationId) {
        return new InventoryTransactionModel<Type.PurchaseOrder>(Optional.empty(),
                Optional.empty(),
                organizationId,
                new Type.PurchaseOrder(null),
                new ArrayList<>());
    }

    public static InventoryTransactionModel<Type.InventoryDeposit> createInventoryDeposit(Long organizationId) {
        return new InventoryTransactionModel<Type.InventoryDeposit>(Optional.empty(),
                Optional.empty(),
                organizationId,
                new Type.InventoryDeposit(),
                new ArrayList<>());
    }

    public void addLog(InventoryLogModel.Type type, int quantity, double price, long productId) {
        final var log = new InventoryLogModel(Optional.empty(),
                Optional.empty(),
                quantity,
                price,
                productId,
                this.organizationId,
                this.id,
                type);

        this.inventoryLogs.add(log);
    }

}
