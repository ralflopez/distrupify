package com.distrupify.dto;

import lombok.Builder;

@Builder
public class InventoryLogDTO {
    public long id;
    public int quantity;
    public double price;
    public String inventoryLogType;
    public String timestamp;
    public ProductDTO product;
}
