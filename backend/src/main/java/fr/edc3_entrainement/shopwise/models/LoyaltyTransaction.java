package fr.edc3_entrainement.shopwise.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class LoyaltyTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private int points;

    private String description;

    private LocalDateTime createdAt = LocalDateTime.now();

    public LoyaltyTransaction() {
    }

    public LoyaltyTransaction(Client client, int points, String description) {
        this.client = client;
        this.points = points;
        this.description = description;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
