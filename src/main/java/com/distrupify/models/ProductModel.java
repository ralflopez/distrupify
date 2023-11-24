package com.distrupify.models;

import jakarta.annotation.Nonnull;

import java.util.Optional;

public record ProductModel(
        @Nonnull Optional<Long> id,
        @Nonnull String brand,
        @Nonnull String name,
        @Nonnull String description,
        double unitPrice,
        int quantity
) {
}
