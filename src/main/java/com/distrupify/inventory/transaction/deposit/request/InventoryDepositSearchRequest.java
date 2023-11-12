package com.distrupify.inventory.transaction.deposit.request;

import com.distrupify.entities.InventoryDeposit;
import lombok.Builder;

import java.util.Date;

@Builder
public class InventoryDepositSearchRequest {
    public Date startDate;
    public Date endDate;

    public boolean hasStartAndEndDate() {
        return startDate != null && endDate != null;
    }

    public boolean hasStartDateOnly() {
        return startDate != null && endDate == null;
    }
}
