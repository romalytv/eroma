package com.kpi.diploma.repo;

import com.kpi.diploma.model.entity.Role;
import com.kpi.diploma.model.entity.types.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleType role);
}
