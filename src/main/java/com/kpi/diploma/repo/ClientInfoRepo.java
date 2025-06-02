package com.kpi.diploma.repo;

import com.kpi.diploma.model.entity.ClientInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientInfoRepo extends JpaRepository<ClientInfo, Long> {
    Optional<ClientInfo> findByClientId(Long clientId);
}