package com.distrupify.entities;

import com.distrupify.dto.ProductDTO;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
@Table(name = "products")
public class Product extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String sku;

    public String brand;

    public String name;

    public String description;

    public BigDecimal price;

    @Column(name = "organization_id", nullable = false)
    public Long organizationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", updatable = false, insertable = false)
    @ToString.Exclude
    public Organization organization;

    public ProductDTO intoDTO() {
        return ProductDTO.builder()
                .id(id)
                .brand(brand)
                .name(name)
                .description(description)
                .sku(sku)
                .price(price.doubleValue())
                .build();
    }
}
