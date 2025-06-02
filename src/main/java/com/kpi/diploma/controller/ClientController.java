package com.kpi.diploma.controller;

import com.kpi.diploma.model.entity.Client;
import com.kpi.diploma.model.entity.ClientInfo;
import com.kpi.diploma.model.entity.Company;
import com.kpi.diploma.model.entity.types.SatisfactionScale;
import com.kpi.diploma.service.ClientService;
import com.kpi.diploma.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final CompanyService companyService;

    // 🧾 1. Перелік клієнтів
    @GetMapping
    public String listClients(Model model) {
        Long companyId = 1L; // ⬅️ тимчасово захардкоджено
        model.addAttribute("clients", clientService.getAllClients());
        return "clients/client-view";
    }

    // ➕ 2. Форма створення нового клієнта
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("satisfactionScale", SatisfactionScale.values());
        return "clients/client-form";
    }

    // 💾 3. Збереження нового або оновленого клієнта
    @PostMapping("/save")
    public String saveClient(@Valid @ModelAttribute Client client,
                             BindingResult result,
                             Model model) {

        if (client.getId() == null) {
            if (clientService.emailExists(client.getEmail())) {
                result.rejectValue("email", "error.client", "Email вже існує");
            }
            if (clientService.phoneExists(client.getPhone())) {
                result.rejectValue("phone", "error.client", "Телефон вже використовується");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("satisfactionScale", SatisfactionScale.values());
            return "clients/client-form";
        }

        clientService.saveClient(client);
        return "redirect:/clients";
    }

    // 📝 4. Форма редагування
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Client client = clientService.getClient(id);
        model.addAttribute("client", client);
        model.addAttribute("satisfactionScale", SatisfactionScale.values());
        return "clients/client-form";
    }

    // ❌ 5. Видалення клієнта
    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return "redirect:/clients";
    }

    // 👁️ 6. Перегляд деталей клієнта
    @GetMapping("/details/{id}")
    public String viewClientDetails(@PathVariable Long id, Model model) {
        Client client = clientService.getClient(id);
        ClientInfo info = clientService.getClientInfo(id);

        model.addAttribute("client", client);
        model.addAttribute("info", info);

        return "clients/client-details";
    }

    @PostMapping("/details/save")
    public String updateClientInfo(@RequestParam Long clientId,
                                   @RequestParam String detailsText,
                                   @RequestParam("image") MultipartFile image) {
        Client client = clientService.getClient(clientId);

        ClientInfo info = clientService.getClientInfo(clientId);
        if (info == null) {
            info = new ClientInfo();
            info.setClient(client);
        }

        info.setDetailsText(detailsText);

        if (!image.isEmpty()) {
            try {
                String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path path = Paths.get("src/main/resources/static/uploads/" + filename);
                Files.createDirectories(path.getParent());
                Files.write(path, image.getBytes());
                info.setImagePath("/uploads/" + filename);
            } catch (IOException e) {
                e.printStackTrace(); // продумай логування
            }
        }

        clientService.saveClientInfo(info);
        return "redirect:/clients/details/" + clientId;
    }

    @GetMapping("/company/{id}")
    public String viewCompany(@PathVariable Long id, Model model) {
        Company company = companyService.getCompany(id);
        model.addAttribute("company", company);
        return "clients/company-view"; // можна також назвати company-detail
    }
}
