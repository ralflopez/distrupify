package com.distrupify.dto;

import com.distrupify.entities.SalesEntity;
import com.distrupify.services.SalesService;
import jakarta.annotation.Nonnull;

import java.util.Date;
import java.util.Optional;

public record SalesDTO(
        @Nonnull Long id,
        @Nonnull Date createdAt,
        @Nonnull Long inventoryTransactionId,
        @Nonnull Optional<Long> customerId,
        @Nonnull Double totalPrice
) {
    public static SalesDTO fromEntity(SalesEntity entity) {
        final Optional<Long> customerId = entity.getCustomerId() == null
                ? Optional.empty()
                : Optional.of(entity.getCustomerId());
        final var totalPrice = SalesService.computeTotalPrice(entity);
        return new SalesDTO(entity.getId(),
                entity.getCreatedAt(),
                entity.getInventoryTransactionId(),
                customerId,
                totalPrice.doubleValue());
    }
}