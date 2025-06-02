package com.kpi.diploma.restcontroller;

import com.kpi.diploma.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthApiController {

    private final UserService userService;

    public AuthApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> handleRegistration(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String email,
            @RequestParam String role,
            @RequestParam Long companyId
    ) {
        try {
            userService.register(username, password, email, role, companyId);
            return ResponseEntity.ok("Користувач успішно зареєстрований");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Помилка: " + e.getMessage());
        }
    }
}
