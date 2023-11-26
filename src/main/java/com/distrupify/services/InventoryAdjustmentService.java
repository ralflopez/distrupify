package com.distrupify.services;

import com.distrupify.entities.InventoryAdjustmentEntity;
import com.distrupify.entities.InventoryLogEntity;
import com.distrupify.models.ProductModel;
import com.distrupify.requests.InventoryAdjustmentCreateRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;

import java.util.stream.Collectors;

@ApplicationScoped
public class InventoryAdjustmentService {

    @Inject
    ProductService productService;

    @Transactional
    public void createInventoryAdjustment(Long organizationId, InventoryAdjustmentCreateRequest request) {
        final var products = productService.findAll(organizationId);

        final var productIdQuantityMap = products.stream()
                .filter(p -> p.id().isPresent())
                .collect(Collectors.toMap(p -> p.id().get(), ProductModel::quantity));

        final var invalidProductLog = request.items
                .stream()
                .filter(l -> !productIdQuantityMap.containsKey(l.productId))
                .findAny();

        if (invalidProductLog.isPresent()) {
            throw new BadRequestException("Invalid product id: " + invalidProductLog.get().productId);
        }

        final var invalidProductQuantityOutgoingLog = request.items
                .stream()
                .filter(l -> l.inventoryLogType.equals(InventoryLogEntity.Type.OUTGOING))
                .filter(l -> productIdQuantityMap.get(l.productId) < l.quantity)
                .findAny();

        if (invalidProductQuantityOutgoingLog.isPresent()) {
            throw new BadRequestException("Invalid product quantity: " +
                    invalidProductQuantityOutgoingLog.get().quantity +
                    " for product id: " +
                    invalidProductQuantityOutgoingLog.get().productId);
        }

        final var inventoryAdjustment = new InventoryAdjustmentEntity(organizationId);
        request.items.forEach(i -> inventoryAdjustment.addLog(i.inventoryLogType, i.productId, i.quantity));
        inventoryAdjustment.persist();
    }
}
