package com.distrupify.dto;

import lombok.Builder;

@Builder
public class InventoryDepositDTO {
    public long id;
    public InventoryTransactionDTO inventoryTransaction;
}
