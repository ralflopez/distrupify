package com.distrupify.requests;

import com.distrupify.validations.ExistingProductId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDepositCreateRequest {
    @Builder
    public static class Item {
        @ExistingProductId(token = true)
        public Long productId;

        @Min(value = 1)
        public Integer quantity;

        @Min(value = 0)
        public Double price;
    }

    @Valid
    public List<Item> items;
}
