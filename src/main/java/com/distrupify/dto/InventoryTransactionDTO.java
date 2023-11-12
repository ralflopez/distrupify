package com.distrupify.dto;

import lombok.Builder;

import java.util.List;

@Builder
public class InventoryTransactionDTO {
    public long id;
    public String inventoryTransactionType;
    public String timestamp;
    public List<InventoryLogDTO> inventoryTransactionLogs;
}
