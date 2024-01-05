package com.distrupify.response;

import com.distrupify.dto.SupplierDTO;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SuppliersResponse(
        @Nonnull @NotNull List<SupplierDTO> suppliers,
        @Nonnull @NotNull Integer pageCount
) {
}
