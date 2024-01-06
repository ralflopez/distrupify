package com.distrupify.services;

import com.distrupify.dto.InventoryTransactionDTO;
import com.distrupify.entities.InventoryTransactionEntity;
import com.distrupify.repository.InventoryTransactionRepository;
import com.distrupify.requests.InventoryTransactionSearchRequest;
import com.distrupify.utils.Pageable;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.text.ParseException;
import java.util.List;

@ApplicationScoped
public class InventoryTransactionService {

    @Inject
    InventoryTransactionRepository inventoryTransactionRepository;

    public List<InventoryTransactionEntity> findAll(Long organizationId, Pageable pageable) {
        return inventoryTransactionRepository.findAll(organizationId, pageable);
    }

    public List<InventoryTransactionDTO> find(Long organizationId,
                                              InventoryTransactionSearchRequest request,
                                              Pageable pageable,
                                              boolean asc) throws ParseException {
        return inventoryTransactionRepository.find(organizationId, request, pageable, asc);
    }

    public void softDelete(@Nonnull Long organizationId, @Nonnull Long transactionId) {
        inventoryTransactionRepository.softDelete(organizationId, transactionId);
    }
}
