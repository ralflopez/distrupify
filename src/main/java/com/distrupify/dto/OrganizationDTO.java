package com.distrupify.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

public record OrganizationDTO(
        @Nonnull @NotNull Long id,
        @Nonnull @NotNull String displayName
) {
}
