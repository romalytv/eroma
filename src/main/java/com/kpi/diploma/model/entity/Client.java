package com.kpi.diploma.model.entity;

import com.kpi.diploma.model.entity.types.DeliveryType;
import com.kpi.diploma.model.entity.types.SatisfactionScale;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ім'я не може бути порожнім")
    @Column(nullable = false)
    private String name;

    @Email(message = "Email має бути коректним")
    @NotBlank(message = "Email не може бути порожнім")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Номер телефону не може бути порожнім")
    @Column(nullable = false)
    private String phone;

    // Адреса не обов’язкова
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "satisfaction_scale")
    private SatisfactionScale satisfactionScale;


    // Зв’язок із компанією
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    // Майбутній зв’язок із продуктами (може бути many-to-many або one-to-many)
    @ManyToMany
    @JoinTable(
            name = "client_products",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;


    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private ClientInfo clientInfo;

    public Client() {}

    public Client(String name, String email, String phone, String address, SatisfactionScale satisfactionScale, Company company) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.satisfactionScale = satisfactionScale;
        this.company = company;
    }
}


