package com.distrupify.services;

import com.distrupify.entities.PurchaseOrderEntity;
import com.distrupify.repository.PurchaseOrderRepository;
import com.distrupify.resources.requests.PurchaseOrderCreateRequest;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class PurchaseOrderService {

    @Inject
    private PurchaseOrderRepository purchaseOrderRepository;

    public void create(@Nonnull Long organizationId, @Nonnull PurchaseOrderCreateRequest request) {
        purchaseOrderRepository.create(organizationId, request);
    }

    public void receive(@Nonnull Long organizationId, @Nonnull Long id) {
        purchaseOrderRepository.receive(organizationId, id);
    }

    public Optional<PurchaseOrderEntity> findByTransactionId(@Nonnull Long organizationId, @Nonnull Long transactionId) {
        return purchaseOrderRepository.findByTransactionId(organizationId, transactionId);
    }

}
