package com.distrupify.resources.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.Date;

@Builder
public class InventoryDepositSearchRequest {
    @NotNull
    public Date startDate;

    public Date endDate;

    public boolean hasStartAndEndDate() {
        return startDate != null && endDate != null;
    }

    public boolean hasStartDateOnly() {
        return startDate != null && endDate == null;
    }
}
