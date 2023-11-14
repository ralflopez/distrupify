package com.distrupify.services;

import com.distrupify.entities.InventoryTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class InventoryTransactionService {
    @Transactional
    public InventoryTransaction createTransaction(Long organizationId, InventoryTransaction.Type type) {
        final var transaction = InventoryTransaction.builder()
                .organizationId(organizationId)
                .inventoryTransactionType(type)
                .build();
        transaction.persist();
        return transaction;
    }
}
