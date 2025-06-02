package com.kpi.diploma.config;

import com.kpi.diploma.service.CustomUserDetailsService;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/api/register", "/login", "/css/**", "/js/**").permitAll()

                        // 🔓 Загальний доступ до dashboard та виходу
                        .requestMatchers("/dashboard", "/logout").authenticated()

                        // 👥 Клієнти — всі ролі
                        .requestMatchers("/clients/**").hasAnyRole("ADMIN", "ACCOUNTANT", "OBSERVER")

                        // 📦 Склад — тільки ADMIN
                        .requestMatchers("/warehouse/**").hasRole("ADMIN")

                        // 📦 Замовлення — всі ролі
                        .requestMatchers("/orders/**").hasAnyRole("ADMIN", "ACCOUNTANT", "OBSERVER")

                        // 💰 Транзакції — тільки ADMIN та ACCOUNTANT
                        .requestMatchers("/transactions/**").hasAnyRole("ADMIN", "ACCOUNTANT")

                        // 📈 Аналітика — всі ролі
                        .requestMatchers("/analytics/**").hasAnyRole("ADMIN", "ACCOUNTANT", "OBSERVER")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access-denied")
                );


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



