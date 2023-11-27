package com.distrupify.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import static com.distrupify.entities.InventoryTransactionEntity.Type.PURCHASE_ORDER;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "purchase_orders")
public class PurchaseOrderEntity extends PanacheEntityBase {
    private static InventoryTransactionEntity.Type INVENTORY_TRANSACTION_TYPE = PURCHASE_ORDER;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "received_at")
    private Date receivedAt;

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

    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", updatable = false, insertable = false)
    @ToString.Exclude
    private SupplierEntity supplier;

    @PrePersist
    @SuppressWarnings("unused")
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = new Date();
        }
    }

    public PurchaseOrderEntity(Long organizationId, Long supplierId) {
        this(organizationId, supplierId, true);
    }

    public PurchaseOrderEntity(Long organizationId, Long supplierId, boolean pending) {
        inventoryTransaction = InventoryTransactionEntity.builder()
                .inventoryTransactionType(INVENTORY_TRANSACTION_TYPE)
                .inventoryLogs(new ArrayList<>())
                .organizationId(organizationId)
                .pending(pending)
                .build();

        this.supplierId = supplierId;
        this.organizationId = organizationId;
    }

    public void addLog(long productId, int quantity, double unitPrice) {
        final var log = InventoryLogEntity.builder()
                .inventoryLogType(InventoryLogEntity.Type.INCOMING)
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
