package com.kpi.diploma.repo;

import com.kpi.diploma.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    List<Product> findByCompanyId(Long companyId);
}
