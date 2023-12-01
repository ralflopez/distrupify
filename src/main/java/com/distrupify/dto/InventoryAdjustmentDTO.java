package com.distrupify.dto;

import com.distrupify.entities.InventoryAdjustmentEntity;
import lombok.AllArgsConstructor;

// TODO: convert dto into record
@AllArgsConstructor
public class InventoryAdjustmentDTO {
    public Long id;
    public String createdAt;
    public InventoryTransactionDTO inventoryTransaction;

    public static InventoryAdjustmentDTO fromEntity(InventoryAdjustmentEntity entity) {
        return new InventoryAdjustmentDTO(entity.getId(),
                entity.getCreatedAt().toString(),
                InventoryTransactionDTO.fromEntity(entity.getInventoryTransaction()));
    }
}
