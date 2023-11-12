package com.distrupify.entities;

import com.distrupify.dto.InventoryLogDTO;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

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
public class InventoryLog extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date timestamp;

    private Integer quantity;

    private BigDecimal price;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", updatable = false, insertable = false)
    private Product product;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", updatable = false, insertable = false)
    @ToString.Exclude
    private Organization organization;

    @Column(name = "inventory_transaction_id", nullable = false)
    private Long inventoryTransactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_transaction_id", updatable = false, insertable = false)
    @ToString.Exclude
    private InventoryTransaction inventoryTransaction;

    @Column(name = "inventory_log_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type inventoryLogType;

    public enum Type {
        INCOMING,
        OUTGOING
    }

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = new Date();
        }
    }

    public InventoryLogDTO intoDTO() {
        final var productDTO = product != null ? product.intoDTO() : null;
        return InventoryLogDTO.builder()
                .id(id)
                .inventoryLogType(inventoryLogType.name())
                .price(price.floatValue())
                .product(productDTO)
                .quantity(quantity)
                .timestamp(timestamp.toString())
                .build();
    }
}
