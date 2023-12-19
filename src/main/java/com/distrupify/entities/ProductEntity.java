package com.distrupify.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
@Table(name = "products")
public class ProductEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sku", unique = true)
    private String sku;

    private String brand;

    private String name;

    @Column(name = "display_name")
    private String displayName;

    private String description;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", updatable = false, insertable = false)
    @ToString.Exclude
    private OrganizationEntity organization;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<InventoryLogEntity> inventoryLogs;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @PrePersist
    @SuppressWarnings("unused")
    protected void onCreate() {
        if (displayName == null) {
            displayName = brand + " " + name + " " + description;
        }

        if (deleted == null) {
            deleted = false;
        }
    }
}
