package com.distrupify.resources.requests;

import jakarta.annotation.Nullable;

public enum ProductSearchFilterBy {
    NAME,
    SKU;

    public static ProductSearchFilterBy getOrDefault(@Nullable ProductSearchFilterBy filterBy) {
        return filterBy != null ? filterBy : NAME;
    }
}
