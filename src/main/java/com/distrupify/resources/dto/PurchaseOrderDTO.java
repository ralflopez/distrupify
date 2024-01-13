package com.distrupify.resources.dto;

import com.distrupify.entities.PurchaseOrderEntity;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record PurchaseOrderDTO(
        @Nonnull @NotNull Long id,
        @Nonnull @NotNull Date createdAt,
        Date receivedAt,
        @Nonnull @NotNull SupplierDTO supplier
) {
    public static PurchaseOrderDTO fromEntity(PurchaseOrderEntity entity) {
        return new PurchaseOrderDTO(entity.getId(),
                entity.getCreatedAt(),
                entity.getReceivedAt(),
                SupplierDTO.fromEntity(entity.getSupplier()));
    }
}
