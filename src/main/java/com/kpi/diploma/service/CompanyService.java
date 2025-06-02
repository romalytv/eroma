package com.kpi.diploma.service;

import com.kpi.diploma.model.entity.Company;
import com.kpi.diploma.repo.CompanyRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepo companyRepo;

    // Отримати всі компанії
    public List<Company> getAllCompanies() {
        return companyRepo.findAll();
    }

    // Отримати компанію за ID
    public Company getCompany(Long id) {
        return companyRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Компанію не знайдено з id: " + id));
    }

    // Зберегти або оновити компанію
    public Company saveCompany(Company company) {
        return companyRepo.save(company);
    }

    // Видалити компанію
    public void deleteCompany(Long id) {
        companyRepo.deleteById(id);
    }

    // Перевірка на унікальність назви (опціонально)
    public boolean nameExists(String name) {
        return companyRepo.existsByName(name);
    }

    // Пошук за частковою назвою (якщо потрібно для фільтра)
    public List<Company> searchByName(String namePart) {
        return companyRepo.findByNameContainingIgnoreCase(namePart);
    }
}
