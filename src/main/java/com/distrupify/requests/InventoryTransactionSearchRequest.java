package com.distrupify.requests;

import com.distrupify.entities.InventoryTransactionEntity;
import com.distrupify.entities.InventoryTransactionEntity.InventoryTransactionStatus;
import com.distrupify.entities.InventoryTransactionEntity.InventoryTransactionType;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryTransactionSearchRequest {
    public String startDate;

    public String endDate;

    @Nonnull public List<InventoryTransactionType> type;

    @Nonnull public List<InventoryTransactionStatus> status;

    public boolean hasStartAndEndDate() {
        return startDate != null && endDate != null;
    }

    public boolean hasStartDateOnly() {
        return startDate != null && endDate == null;
    }
}
