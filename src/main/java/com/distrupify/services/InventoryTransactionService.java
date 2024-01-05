package com.distrupify.services;

import com.distrupify.entities.InventoryTransactionEntity;
import com.distrupify.entities.InventoryTransactionEntity$;
import com.distrupify.repository.InventoryTransactionRepository;
import com.distrupify.requests.InventoryTransactionSearchRequest;
import com.distrupify.utils.DateUtil;
import com.distrupify.utils.Pageable;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.text.ParseException;
import java.util.Comparator;
import java.util.List;

import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.of;

@ApplicationScoped
public class InventoryTransactionService {

    @Inject
    InventoryTransactionRepository inventoryTransactionRepository;

    public List<InventoryTransactionEntity> findAll(Long organizationId, Pageable pageable) {
        return inventoryTransactionRepository.findAll(organizationId, pageable);
    }

    public List<InventoryTransactionEntity> searchAll(Long organizationId,
                                                      InventoryTransactionSearchRequest request,
                                                      Pageable pageable,
                                                      boolean asc) throws ParseException {
        return inventoryTransactionRepository.searchAll(organizationId, request, pageable, asc);
    }

    public void softDelete(@Nonnull Long organizationId, @Nonnull Long transactionId) {
        inventoryTransactionRepository.softDelete(organizationId, transactionId);
    }
}
