package com.distrupify.repository;

import com.distrupify.entities.*;
import com.distrupify.models.InventoryLogModel;
import com.distrupify.models.InventoryTransactionModel;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

import static com.distrupify.entities.InventoryLogEntity.Type.INCOMING;
import static com.distrupify.entities.InventoryLogEntity.Type.OUTGOING;
import static com.distrupify.entities.InventoryTransactionEntity.Type.*;

@ApplicationScoped
public class InventoryTransactionRepository {

    @Transactional
    public <T extends InventoryTransactionModel.Type> void persist(@Nonnull InventoryTransactionModel<T> inventoryTransactionModel) {
        InventoryTransactionEntity.Type type = switch (inventoryTransactionModel.details()) {
            case InventoryTransactionModel.Type.InventoryDeposit ignored -> DEPOSIT;
            case InventoryTransactionModel.Type.PurchaseOrder ignored -> PURCHASE_ORDER;
            case InventoryTransactionModel.Type.InventoryWithdraw ignored -> WITHDRAW;
        };

        final var inventoryTransaction = InventoryTransactionEntity.builder()
                .organizationId(inventoryTransactionModel.organizationId())
                .inventoryTransactionType(type)
                .pending(inventoryTransactionModel.isPending())
                .build();
        inventoryTransaction.persist();

        inventoryTransactionModel.inventoryLogs()
                .stream()
                .map(l -> createInventoryLog(inventoryTransaction.getId(), l))
                .forEach(l -> l.persist());

        final var transactionWrapper = switch (type) {
            case DEPOSIT -> InventoryDepositEntity.builder()
                    .inventoryTransactionId(inventoryTransaction.getId())
                    .organizationId(inventoryTransactionModel.organizationId())
                    .build();
            case PURCHASE_ORDER -> PurchaseOrderEntity.builder()
                    .inventoryTransactionId(inventoryTransaction.getId())
                    .organizationId(inventoryTransactionModel.organizationId())
                    .build();
            case WITHDRAW -> InventoryWithdrawEntity.builder()
                    .inventoryTransactionId(inventoryTransaction.getId())
                    .organizationId(inventoryTransactionModel.organizationId())
                    .build();
        };

        transactionWrapper.persist();
    }

    private InventoryLogEntity createInventoryLog(Long inventoryTransactionId, InventoryLogModel inventoryLogModel) {
        final var type = switch (inventoryLogModel.invetoryLogType()) {
            case InventoryLogModel.Type.Incoming ignored -> INCOMING;
            case InventoryLogModel.Type.Outgoing ignored -> OUTGOING;
        };

        return InventoryLogEntity.builder()
                .organizationId(inventoryLogModel.organizationId())
                .inventoryLogType(type)
                .inventoryTransactionId(inventoryTransactionId)
                .unitPrice(BigDecimal.valueOf(inventoryLogModel.unitPrice()))
                .quantity(inventoryLogModel.quantity())
                .productId(inventoryLogModel.productId())
                .build();
    }
}
