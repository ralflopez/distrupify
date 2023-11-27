package com.distrupify.dto;

import com.distrupify.entities.InventoryLogEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InventoryLogDTO {
    public long id;
    public int quantity;
    public double price;
    public String inventoryLogType;
    public String timestamp;
    public ProductDTO product;

    public static InventoryLogDTO fromEntity(InventoryLogEntity entity) {
        return new InventoryLogDTO(entity.getId(),
                entity.getQuantity(),
                entity.getUnitPrice().doubleValue(),
                entity.getInventoryLogType().name(),
                entity.getTimestamp().toString(),
                ProductDTO.fromEntity(entity.getProduct()));
    }
}
