package com.distrupify.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
@Table(name = "inventory_logs")
public class InventoryLogEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date timestamp;

    private Integer quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", updatable = false, insertable = false)
    private ProductEntity product;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", updatable = false, insertable = false)
    @ToString.Exclude
    private OrganizationEntity organization;

    @Column(name = "inventory_transaction_id", nullable = false)
    private Long inventoryTransactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_transaction_id", updatable = false, insertable = false)
    @ToString.Exclude
    private InventoryTransactionEntity inventoryTransaction;

    @Column(name = "inventory_log_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type inventoryLogType;

    public enum Type {
        INCOMING,
        OUTGOING
    }

    @PrePersist
    @SuppressWarnings("unused")
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = new Date();
        }
    }
}
