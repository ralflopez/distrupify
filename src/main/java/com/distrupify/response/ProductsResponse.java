package com.distrupify.response;

import com.distrupify.dto.ProductDTO;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProductsResponse(
        @Nonnull @NotNull List<ProductDTO> products,
        @Nonnull @NotNull Integer pageCount
) {
}
