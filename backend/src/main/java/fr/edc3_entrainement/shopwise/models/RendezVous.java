package fr.edc3_entrainement.shopwise.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rendez_vous")
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "date_rendez_vous", nullable = false)
    private LocalDateTime dateRendezVous;

    @Column(nullable = false)
    private String service;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RendezVousStatus statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnore
    private Client client;

    public RendezVous() {
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getDateRendezVous() {
        return dateRendezVous;
    }

    public void setDateRendezVous(LocalDateTime dateRendezVous) {
        this.dateRendezVous = dateRendezVous;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public RendezVousStatus getStatut() {
        return statut;
    }

    public void setStatut(RendezVousStatus statut) {
        this.statut = statut;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
