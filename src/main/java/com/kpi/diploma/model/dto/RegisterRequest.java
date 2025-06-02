package com.kpi.diploma.model.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String role;
    private Long companyId;
}
