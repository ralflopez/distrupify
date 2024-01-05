package com.distrupify.response;

import com.distrupify.dto.SalesDTO;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SalesResponse(
        @Nonnull @NotNull List<SalesDTO> sales,
        @Nonnull @NotNull Integer pageCount
) {
}
