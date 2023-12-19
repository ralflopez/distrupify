package com.distrupify.requests;

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
public class SalesCreateRequest {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        public Long productId;

        @Min(value = 1)
        public Integer quantity;

        @Min(value = 0)
        public Double unitPrice;
    }

    @Valid
    public List<Item> items;

    public Long customerId;
}
