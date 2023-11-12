package com.distrupify.dto;

import lombok.Builder;

@Builder
public class ProductDTO {
    public long id;
    public String sku;
    public String brand;
    public String name;
    public String description;
    public double price;
}
