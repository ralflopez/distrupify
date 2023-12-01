package com.distrupify.dto;

import lombok.Builder;

// TODO: convert response into records
@Builder
public class InventoryDepositDTO {
    public long id;
    public InventoryTransactionDTO inventoryTransaction;
}
