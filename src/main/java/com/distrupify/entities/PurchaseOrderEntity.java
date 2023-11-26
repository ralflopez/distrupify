package com.distrupify.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "purchase_orders")
public class PurchaseOrderEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "received_at", nullable = true)
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

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = new Date();
        }
    }

    public PurchaseOrderEntity(Long organizationId) {
        this(organizationId, true);
    }

    public PurchaseOrderEntity(Long organizationId, boolean pending) {
        inventoryTransaction = InventoryTransactionEntity.builder()
                .inventoryTransactionType(InventoryTransactionEntity.Type.PURCHASE_ORDER)
                .inventoryLogs(new ArrayList<>())
                .organizationId(organizationId)
                .pending(pending)
                .build();

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
        inventoryTransaction.persist();
        inventoryTransactionId = inventoryTransaction.getId();

        inventoryTransaction.getInventoryLogs()
                .stream()
                .peek(l -> l.setInventoryTransactionId(inventoryTransaction.getId()))
                .forEach(l -> l.persist());

        super.persist();
    }
}
