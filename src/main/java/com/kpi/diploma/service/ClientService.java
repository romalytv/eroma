package com.kpi.diploma.service;

import com.kpi.diploma.model.entity.Client;
import com.kpi.diploma.model.entity.ClientInfo;
import com.kpi.diploma.model.entity.types.SatisfactionScale;
import com.kpi.diploma.repo.ClientInfoRepo;
import com.kpi.diploma.repo.ClientRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepo clientRepo;
    private final ClientInfoRepo clientInfoRepo;

    // Отримати всіх клієнтів певної компанії
    public List<Client> getAllClientsByCompany(Long companyId) {
        return clientRepo.findByCompanyId(companyId);
    }

    public List<Client> getAllClients() {
        return clientRepo.findAll();
    }

    // Отримати одного клієнта за ID
    public Client getClient(Long id) {
        return clientRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Клієнта не знайдено з id: " + id));
    }

    // Створити або оновити клієнта
    public Client saveClient(Client client) {
        return clientRepo.save(client);
    }

    // Видалити клієнта та його info (через Cascade)
    public void deleteClient(Long id) {
        clientRepo.deleteById(id);
    }

    // Отримати ClientInfo за clientId
    public ClientInfo getClientInfo(Long clientId) {
        return clientInfoRepo.findByClientId(clientId).orElse(null);
    }

    // Зберегти або оновити ClientInfo
    public ClientInfo saveClientInfo(ClientInfo info) {
        return clientInfoRepo.save(info);
    }

    // Видалити ClientInfo вручну
    public void deleteClientInfo(Long id) {
        clientInfoRepo.deleteById(id);
    }

    // Перевірка унікальності email
    public boolean emailExists(String email) {
        return clientRepo.existsByEmail(email);
    }

    // Перевірка унікальності телефону
    public boolean phoneExists(String phone) {
        return clientRepo.existsByPhone(phone);
    }

    public Map<String, Long> getFeedbackStats() {
        List<Client> clients = clientRepo.findAll();

        long veryHappy = clients.stream()
                .filter(c -> c.getSatisfactionScale() == SatisfactionScale.VERY_HAPPY)
                .count();

        long happy = clients.stream()
                .filter(c -> c.getSatisfactionScale() == SatisfactionScale.HAPPY)
                .count();

        long neutral = clients.stream()
                .filter(c -> c.getSatisfactionScale() == SatisfactionScale.NEUTRAL)
                .count();

        long unhappy = clients.stream()
                .filter(c -> c.getSatisfactionScale() == SatisfactionScale.UNHAPPY)
                .count();

        long veryUnhappy = clients.stream()
                .filter(c -> c.getSatisfactionScale() == SatisfactionScale.VERY_UNHAPPY)
                .count();

        return Map.of(
                "veryHappy", veryHappy,
                "happy", happy,
                "neutral", neutral,
                "unhappy", unhappy,
                "veryUnhappy", veryUnhappy
        );
    }


}


