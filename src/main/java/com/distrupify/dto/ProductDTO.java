package com.distrupify.dto;

public record ProductDTO(
        long id,
        String sku,
        String brand,
        String name,
        String description,
        double unitPrice,
        int quantity
) {}
