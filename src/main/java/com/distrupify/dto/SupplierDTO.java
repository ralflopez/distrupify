package com.distrupify.dto;

import com.distrupify.entities.SupplierEntity;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

public record SupplierDTO(
        @Nonnull @NotNull Long id,
        @Nonnull @NotNull String name,
        @Nonnull @NotNull String address,
        @Nonnull @NotNull String contactNumber
) {
    public static SupplierDTO fromEntity(SupplierEntity entity) {
        return new SupplierDTO(entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getContactNumber());
    }
}
