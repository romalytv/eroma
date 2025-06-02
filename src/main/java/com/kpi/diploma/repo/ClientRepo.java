package com.kpi.diploma.repo;

import com.kpi.diploma.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {

    List<Client> findByCompanyId(Long companyId);

    Optional<Client> findByEmail(String email);

    List<Client> findByNameContainingIgnoreCase(String name);

    boolean existsByEmail(String email); // додай цей метод

    boolean existsByPhone(String phone);
}
