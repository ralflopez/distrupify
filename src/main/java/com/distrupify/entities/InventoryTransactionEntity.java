package com.distrupify.entities;

import com.distrupify.dto.InventoryTransactionDTO;
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

    public enum Type {
        DEPOSIT,
        PURCHASE_ORDER
    }

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = new Date();
        }
    }

    public InventoryTransactionDTO intoDTO() {
        return InventoryTransactionDTO.builder()
                .id(id)
                .inventoryTransactionLogs(inventoryLogs.stream().map(InventoryLogEntity::intoDTO).toList())
                .inventoryTransactionType(inventoryTransactionType.name())
                .timestamp(timestamp.toString())
                .build();
    }
}
