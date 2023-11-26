package com.distrupify.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
@Table(name = "purchase_orders")
public class PurchaseOrderEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date timestamp;

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
}
