package com.distrupify.organization;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "organization")
public class Organization extends PanacheEntity {
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "display_name")
    private String displayName;
}
