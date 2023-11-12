package com.distrupify.inventory.transaction.deposit.request;

import com.distrupify.validation.ExistingProductId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.Builder;

import java.util.List;

@Builder
public class CreateInventoryDepositRequest {
    @Builder
    public static class Item {
        @ExistingProductId
        public Long productId;

        @Min(value = 1)
        public Integer quantity;

        @Min(value = 0)
        public Double price;
    }

    @Valid
    public List<Item> items;
}
