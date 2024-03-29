package com.distrupify.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import static com.distrupify.entities.InventoryTransactionEntity.InventoryTransactionType.ADJUSTMENT;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
@Table(name = "inventory_adjustments")
public class InventoryAdjustmentEntity extends PanacheEntityBase {
    private static InventoryTransactionEntity.InventoryTransactionType INVENTORY_TRANSACTION_TYPE = ADJUSTMENT;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;
    public static final String ORGANIZATION_ID = "organizationId";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", updatable = false, insertable = false)
    @ToString.Exclude
    private OrganizationEntity organization;

    @Column(name = "inventory_transaction_id", nullable = false)
    private Long inventoryTransactionId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_transaction_id", updatable = false, insertable = false)
    @ToString.Exclude
    private InventoryTransactionEntity inventoryTransaction;

    @PrePersist
    @SuppressWarnings("unused")
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = new Date();
        }
    }

    public InventoryAdjustmentEntity(Long organizationId) {
        inventoryTransaction = InventoryTransactionEntity.builder()
                .inventoryTransactionType(INVENTORY_TRANSACTION_TYPE)
                .inventoryLogs(new ArrayList<>())
                .organizationId(organizationId)
                .status(InventoryTransactionEntity.InventoryTransactionStatus.VALID)
                .build();

        this.organizationId = organizationId;
    }

    public void addLog(InventoryLogEntity.InventoryLogType inventoryLogType, long productId,
                       int quantity, double unitPrice) {
        final var log = InventoryLogEntity.builder()
                .inventoryLogType(inventoryLogType)
                .unitPrice(BigDecimal.valueOf(unitPrice))
                .organizationId(organizationId)
                .productId(productId)
                .quantity(quantity)
                .build();

        inventoryTransaction.getInventoryLogs().add(log);
    }

    @Override
    @Transactional
    public void persist() {
        inventoryTransaction.setTimestamp(createdAt);
        inventoryTransaction.persist();
        inventoryTransactionId = inventoryTransaction.getId();

        inventoryTransaction.getInventoryLogs()
                .stream()
                .peek(l -> l.setInventoryTransactionId(inventoryTransaction.getId()))
                .forEach(l -> l.persist());

        super.persist();
    }
}
