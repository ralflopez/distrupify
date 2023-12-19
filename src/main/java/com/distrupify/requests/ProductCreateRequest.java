package com.distrupify.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ProductCreateRequest {
    @NotNull
    @NotEmpty
    public String sku;

    @NotNull
    @NotEmpty
    public String brand;

    @NotNull
    @NotEmpty
    public String name;

    @NotNull
    public String description;

    @Min(value = 0)
    public Float unitPrice;
}
