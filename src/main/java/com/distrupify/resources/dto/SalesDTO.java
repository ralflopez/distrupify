package com.distrupify.resources.dto;

import com.distrupify.entities.SalesEntity;
import com.distrupify.services.SalesService;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.Optional;

public record SalesDTO(
        @Nonnull @NotNull Long id,
        @Nonnull @NotNull Date createdAt,
        @Nonnull @NotNull Long inventoryTransactionId,
        @Nonnull @NotNull Optional<Long> customerId,
        @Nonnull @NotNull Double totalPrice
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