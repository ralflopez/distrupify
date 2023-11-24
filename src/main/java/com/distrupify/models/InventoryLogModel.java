package com.distrupify.models;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.util.Date;
import java.util.Optional;

@Builder
public record InventoryLogModel(
        Optional<Long> id,
        Optional<Date> timestamp,
        int quantity,
        double unitPrice,
        long productId,
        long organizationId,
        @Nonnull Optional<Long> inventoryTransactionId,
        @Nonnull Type invetoryLogType
) {
    public sealed interface Type {
        Type INCOMING = new Incoming();
        record Incoming() implements Type {}

        Type OUTGOING = new Outgoing();
        record Outgoing() implements Type {}
    }
}
