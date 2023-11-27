package com.distrupify.dto;

import com.distrupify.entities.InventoryTransactionEntity;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class InventoryTransactionDTO {
    public long id;
    public String inventoryTransactionType;
    public String timestamp;
    public List<InventoryLogDTO> inventoryTransactionLogs;

    public static InventoryTransactionDTO fromEntity(InventoryTransactionEntity entity) {
        return new InventoryTransactionDTO(entity.getId(),
                entity.getInventoryTransactionType().name(),
                entity.getTimestamp().toString(),
                entity.getInventoryLogs()
                        .stream()
                        .map(InventoryLogDTO::fromEntity).toList());
    }
}
