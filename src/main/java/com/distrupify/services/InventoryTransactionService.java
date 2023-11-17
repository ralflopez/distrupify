package com.distrupify.services;

import com.distrupify.models.InventoryTransactionModel;
import com.distrupify.repository.InventoryTransactionRepository;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class InventoryTransactionService {
    @Inject
    InventoryTransactionRepository inventoryTransactionRepository;

    public <T extends InventoryTransactionModel.Type> void persist(
            @Nonnull InventoryTransactionModel<T> inventoryTransactionModel) {
        inventoryTransactionRepository.persist(inventoryTransactionModel);
    }
}
