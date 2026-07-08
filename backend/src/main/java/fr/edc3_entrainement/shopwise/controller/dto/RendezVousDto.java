package fr.edc3_entrainement.shopwise.controller.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class RendezVousDto {
    private UUID id;
    private LocalDateTime dateRendezVous;
    private String service;
    private String statut;
    private UUID clientId;
    private String clientNom;

    public RendezVousDto(UUID id, LocalDateTime dateRendezVous, String service, String statut, UUID clientId, String clientNom) {
        this.id = id;
        this.dateRendezVous = dateRendezVous;
        this.service = service;
        this.statut = statut;
        this.clientId = clientId;
        this.clientNom = clientNom;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public LocalDateTime getDateRendezVous() { return dateRendezVous; }
    public void setDateRendezVous(LocalDateTime dateRendezVous) { this.dateRendezVous = dateRendezVous; }
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public UUID getClientId() { return clientId; }
    public void setClientId(UUID clientId) { this.clientId = clientId; }
    public String getClientNom() { return clientNom; }
    public void setClientNom(String clientNom) { this.clientNom = clientNom; }
}
