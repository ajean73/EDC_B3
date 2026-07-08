package fr.edc3_entrainement.shopwise.controller.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateRendezVousDto {
    @NotNull(message = "Le client est obligatoire.")
    private UUID clientId;

    @NotNull(message = "La date du rendez-vous est obligatoire.")
    @FutureOrPresent(message = "La date du rendez-vous doit etre dans le present ou le futur.")
    private LocalDateTime dateRendezVous;

    @NotBlank(message = "Le service est obligatoire.")
    private String service;

    public UUID getClientId() { return clientId; }
    public void setClientId(UUID clientId) { this.clientId = clientId; }
    public LocalDateTime getDateRendezVous() { return dateRendezVous; }
    public void setDateRendezVous(LocalDateTime dateRendezVous) { this.dateRendezVous = dateRendezVous; }
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
}
