package com.distrupify.dto;

import com.distrupify.entities.SalesEntity;
import jakarta.annotation.Nonnull;

import java.util.Date;
import java.util.Optional;

public record SalesDTO(
        @Nonnull Long id,
        @Nonnull Date createdAt,
        @Nonnull Long organizationId,
        @Nonnull Long inventoryTransactionId,
        @Nonnull Optional<Long> customerId
) {
    public static SalesDTO fromEntity(SalesEntity entity) {
        final Optional<Long> customerId = entity.getCustomerId() == null
                ? Optional.empty()
                : Optional.of(entity.getCustomerId());
        return new SalesDTO(entity.getId(),
                entity.getCreatedAt(),
                entity.getOrganizationId(),
                entity.getInventoryTransactionId(),
                customerId);
    }
}