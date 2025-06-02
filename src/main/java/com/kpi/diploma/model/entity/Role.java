package com.kpi.diploma.model.entity;

import com.kpi.diploma.model.entity.types.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users;

    public Role() {}

    public Role(RoleType name) {
        this.name = name;
    }

    public Role(RoleType name, List<User> users) {
        this.name = name;
        this.users = users;
    }
}

