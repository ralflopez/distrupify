package com.distrupify.dto;

import com.distrupify.entities.ProductEntity;
import com.distrupify.entities.SupplierEntity;
import jakarta.annotation.Nonnull;

public record SupplierDTO(
        @Nonnull Long id,
        @Nonnull String name,
        @Nonnull String address,
        @Nonnull String contactNumber
) {
    public static SupplierDTO fromEntity(SupplierEntity entity) {
        return new SupplierDTO(entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getContactNumber());
    }
}
