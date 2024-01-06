package com.distrupify.services;

import com.distrupify.resources.dto.InventoryTransactionDTO;
import com.distrupify.entities.InventoryTransactionEntity;
import com.distrupify.repository.InventoryTransactionRepository;
import com.distrupify.resources.requests.InventoryTransactionSearchRequest;
import com.distrupify.resources.requests.ProductSearchFilterBy;
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

    public List<InventoryTransactionEntity> findAll(@Nonnull Long organizationId, @Nonnull Pageable pageable) {
        return inventoryTransactionRepository.findAll(organizationId, pageable);
    }

    public List<InventoryTransactionDTO> find(@Nonnull Long organizationId,
                                              @Nonnull InventoryTransactionSearchRequest request,
                                              @Nonnull Pageable pageable,
                                              boolean asc) throws ParseException {
        return inventoryTransactionRepository.find(organizationId, request, pageable, asc);
    }

    public int getPageCount(@Nonnull Long organizationId,
                            @Nonnull InventoryTransactionSearchRequest request,
                            boolean asc,
                            int pageSize) throws ParseException {
        final var productCount = inventoryTransactionRepository.getCount(organizationId, request, asc);
        return (int) Math.ceilDiv(productCount, pageSize);
    }

    public void softDelete(@Nonnull Long organizationId, @Nonnull Long transactionId) {
        inventoryTransactionRepository.softDelete(organizationId, transactionId);
    }
}
