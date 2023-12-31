package com.distrupify.entities;

import com.distrupify.dto.OrganizationDTO;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
@Table(name = "organizations")
public class OrganizationEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    public String name;

    @Column(name = "display_name")
    public String displayName;

    public OrganizationDTO intoDTO() {
        return OrganizationDTO.builder()
                .id(id)
                .displayName(displayName)
                .build();
    }
}
