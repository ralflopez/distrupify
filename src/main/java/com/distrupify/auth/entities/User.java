package com.distrupify.auth.entities;

import com.distrupify.auth.dtos.UserDTO;
import com.distrupify.entities.Organization;
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.persistence.criteria.JoinType;
import lombok.*;

import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.of;
import static jakarta.persistence.criteria.JoinType.INNER;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
@Table(name = "users")
public class User extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", updatable = false, insertable = false)
    @ToString.Exclude
    private Organization organization;

    public UserDTO intoDTO() {
        final var organizationDTO = organization != null ? organization.intoDTO() : null;
        return UserDTO.builder()
                .id(id)
                .email(email)
                .name(name)
                .organization(organizationDTO)
                .build();
    }

    public static final class Joins {
        public static final StreamConfiguration<User> USER_IJ_ORGANIZATION = of(User.class).joining(User$.organization, INNER);
    }
}
