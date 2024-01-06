package com.distrupify.resources.requests;

import com.distrupify.validations.ExistingProductId;
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
public class InventoryDepositCreateRequest {
    @Builder
    public static class InventoryDepositCreateRequestItem {
        @ExistingProductId(token = true)
        @NotNull
        public Long productId;

        @Min(value = 1)
        @NotNull
        public Integer quantity;

        @Min(value = 0)
        @NotNull
        public Double price;
    }

    @Valid
    @NotNull
    public List<InventoryDepositCreateRequestItem> items;
}
