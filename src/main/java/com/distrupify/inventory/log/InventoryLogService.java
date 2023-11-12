package com.distrupify.inventory.log;

import com.distrupify.entities.InventoryLog;
import com.distrupify.entities.InventoryTransaction;
import com.distrupify.inventory.transaction.deposit.request.CreateInventoryDepositRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

@ApplicationScoped
public class InventoryLogService {
    @Transactional
    public InventoryLog createInventoryLog(Long organizationId,
                                                    InventoryLog.Type type,
                                                    InventoryTransaction inventoryTransaction,
                                                    CreateInventoryDepositRequest.Item item) {
        final var log =  InventoryLog.builder()
                .organizationId(organizationId)
                .inventoryLogType(type)
                .inventoryTransactionId(inventoryTransaction.getId())
                .price(BigDecimal.valueOf(item.price))
                .quantity(item.quantity)
                .productId(item.productId)
                .build();
        log.persist();
        return log;
    }
}
