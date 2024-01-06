package com.distrupify.resources.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderCreateRequest {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PurchaseOrderCreateRequestItem {
        @NotNull
        public Long productId;

        @Min(value = 1)
        @NotNull
        public Integer quantity;

        @Min(value = 0)
        @NotNull
        public Double unitPrice;
    }

    @Valid
    @NotNull
    public List<PurchaseOrderCreateRequestItem> items;

    @NotNull
    public Long supplierId;
}
