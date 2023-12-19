package com.distrupify.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
@Table(name = "inventory_transactions")
public class InventoryTransactionEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inventory_transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    public InventoryTransactionEntity.Type inventoryTransactionType;

    @Column(name = "timestamp", nullable = false)
    public Date timestamp;
    public static final String TIMESTAMP = "timestamp";

    @Column(name = "organization_id", nullable = false)
    public Long organizationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", updatable = false, insertable = false)
    @ToString.Exclude
    public OrganizationEntity organization;

    @OneToMany(mappedBy = "inventoryTransaction", fetch = FetchType.LAZY)
    @ToString.Exclude
    public List<InventoryLogEntity> inventoryLogs;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    public Status status;

    public enum Status {
        PENDING,
        DELETED,
        VALID,
    }

    public enum Type {
        PURCHASE_ORDER,
        SALES,
        ADJUSTMENT
    }

    @PrePersist
    @SuppressWarnings("unused")
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = new Date();
        }

        if (status == null) {
            status = Status.VALID;
        }
    }
}
