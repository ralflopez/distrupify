package com.distrupify.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;

import static com.distrupify.entities.InventoryTransactionEntity.Type.WITHDRAWAL;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
@Table(name = "inventory_withdrawals")
public class InventoryWithdrawalEntity extends PanacheEntityBase {
    private static InventoryTransactionEntity.Type INVENTORY_TRANSACTION_TYPE = WITHDRAWAL;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public InventoryWithdrawalEntity(Long organizationId) {
        this(organizationId, false);
    }

    public InventoryWithdrawalEntity(Long organizationId, boolean pending) {
        inventoryTransaction = InventoryTransactionEntity.builder()
                .inventoryTransactionType(INVENTORY_TRANSACTION_TYPE)
                .inventoryLogs(new ArrayList<>())
                .organizationId(organizationId)
                .pending(pending)
                .build();

        this.organizationId = organizationId;
    }

    public void addLog(long productId, int quantity, double unitPrice) {
        final var log = InventoryLogEntity.builder()
                .inventoryLogType(InventoryLogEntity.Type.OUTGOING)
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
