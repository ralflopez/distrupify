package com.distrupify.requests;

import jakarta.validation.constraints.NotNull;

public class ProductSearchRequest {
    public enum FilterBy {
        NAME,
        SKU
    };

    @NotNull
    public FilterBy filter;

    @NotNull
    public String searchString;
}
