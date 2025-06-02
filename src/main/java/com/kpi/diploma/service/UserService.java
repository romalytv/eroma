package com.kpi.diploma.service;

import com.kpi.diploma.model.entity.Company;
import com.kpi.diploma.model.entity.User;
import com.kpi.diploma.model.entity.Role;
import com.kpi.diploma.model.entity.types.RoleType;
import com.kpi.diploma.repo.CompanyRepo;
import com.kpi.diploma.repo.RoleRepo;
import com.kpi.diploma.repo.UserRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepo userRepository;
    private final RoleRepo roleRepository;
    private final CompanyRepo companyRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepository,
                       RoleRepo roleRepository,
                       CompanyRepo companyRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String username, String rawPassword, String email, String roleName, Long companyId) {
        Role role = roleRepository.findByName(RoleType.valueOf(roleName))
                .orElseThrow(() -> new RuntimeException("Роль не знайдена"));

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Компанія не знайдена"));

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Користувач з таким іменем вже існує");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEmail(email);
        user.setRole(role);
        user.setCompany(company);

        userRepository.save(user);
    }
}

