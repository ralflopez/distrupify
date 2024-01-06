package com.distrupify.services;

import com.distrupify.entities.InventoryAdjustmentEntity;
import com.distrupify.repository.InventoryAdjustmentRepository;
import com.distrupify.resources.requests.InventoryAdjustmentCreateRequest;
import com.distrupify.utils.Pageable;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class InventoryAdjustmentService {

    @Inject
    InventoryAdjustmentRepository inventoryAdjustmentRepository;

    /**
     * Note
     * .getInventoryLogs() throws a LazyInitializationException outside this transaction
     * <a href="https://stackoverflow.com/questions/26507446/how-to-resolve-lazyinitializationexception-in-spring-data-jpa#:~:text=if%20you%20try%20to%20get%20the%20lazy%20collection%2C%20you%20will%20get%20that%20exception">Solution</a>
     */
    @Transactional
    public List<InventoryAdjustmentEntity> findAll(@Nonnull Long organizationId, @Nonnull Pageable pageable) {
        return inventoryAdjustmentRepository.findAll(organizationId, pageable);
    }

    public int getPageCount(@Nonnull Long organizationId, int pageSize) {
        final var productCount = inventoryAdjustmentRepository.getCount(organizationId);
        return (int) Math.ceilDiv(productCount, pageSize);
    }

    @Transactional
    public void createInventoryAdjustment(@Nonnull Long organizationId, @Nonnull InventoryAdjustmentCreateRequest request) {
        inventoryAdjustmentRepository.create(organizationId, request);
    }
}
