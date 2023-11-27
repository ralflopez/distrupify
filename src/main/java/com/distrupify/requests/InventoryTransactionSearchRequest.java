package com.distrupify.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryTransactionSearchRequest {
    public String startDate;
    public String endDate;

    public boolean hasStartAndEndDate() {
        return startDate != null && endDate != null;
    }

    public boolean hasStartDateOnly() {
        return startDate != null && endDate == null;
    }
}
