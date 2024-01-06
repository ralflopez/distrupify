package com.distrupify.resources.response;

import com.distrupify.resources.dto.SupplierDTO;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SuppliersResponse(
        @Nonnull @NotNull List<SupplierDTO> suppliers,
        @Nonnull @NotNull Integer pageCount
) {
}
