package com.distrupify.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import static com.distrupify.entities.InventoryTransactionEntity.InventoryTransactionType.SALES;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
@Table(name = "sales")
public class SalesEntity extends PanacheEntityBase {
    private static InventoryTransactionEntity.InventoryTransactionType INVENTORY_TRANSACTION_TYPE = SALES;

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
    public static final String INVENTORY_TRANSACTION = "inventoryTransaction";

    @Column(name = "customer_id")
    private Long customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", updatable = false, insertable = false)
    @ToString.Exclude
    private CustomerEntity customer;

    @SuppressWarnings("unused")
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = new Date();
        }
    }

    public SalesEntity(Long organizationId, Long customerId) {
        this(organizationId, customerId, false);
    }

    public SalesEntity(Long organizationId, Long customerId, boolean pending) {
        inventoryTransaction = InventoryTransactionEntity.builder()
                .inventoryTransactionType(INVENTORY_TRANSACTION_TYPE)
                .inventoryLogs(new ArrayList<>())
                .organizationId(organizationId)
                .status(pending ? InventoryTransactionEntity.InventoryTransactionStatus.PENDING : InventoryTransactionEntity.InventoryTransactionStatus.VALID)
                .build();

        this.customerId = customerId;
        this.organizationId = organizationId;
    }

    public void addLog(long productId, int quantity, double unitPrice) {
        final var log = InventoryLogEntity.builder()
                .inventoryLogType(InventoryLogEntity.InventoryLogType.OUTGOING)
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
