package fr.edc3_entrainement.shopwise.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelesTest {

    @Test
    @DisplayName("doit manipuler Client")
    void client_gettersSetters() {
        Client client = new Client();
        UUID id = UUID.randomUUID();

        client.setId(id);
        client.setNom("Alice");
        client.setEmail("a@a.com");
        client.setTelephone("0102030405");
        client.setPassword("secret");
        client.setPointsFidelite(20);

        assertEquals(id, client.getId());
        assertEquals("Alice", client.getNom());
        assertEquals("a@a.com", client.getEmail());
        assertEquals("0102030405", client.getTelephone());
        assertEquals("secret", client.getPassword());
        assertEquals(20, client.getPointsFidelite());
    }

    @Test
    @DisplayName("doit manipuler RendezVous")
    void rendezVous_gettersSetters() {
        Client client = new Client();
        client.setId(UUID.randomUUID());

        RendezVous rendezVous = new RendezVous();
        UUID id = UUID.randomUUID();
        LocalDateTime date = LocalDateTime.now();

        rendezVous.setId(id);
        rendezVous.setClient(client);
        rendezVous.setDateRendezVous(date);
        rendezVous.setService("Atelier");
        rendezVous.setStatut(RendezVousStatus.PLANIFIE);

        assertEquals(id, rendezVous.getId());
        assertEquals(client, rendezVous.getClient());
        assertEquals(date, rendezVous.getDateRendezVous());
        assertEquals("Atelier", rendezVous.getService());
        assertEquals(RendezVousStatus.PLANIFIE, rendezVous.getStatut());
    }

    @Test
    @DisplayName("doit manipuler LoyaltyTransaction")
    void loyaltyTransaction_gettersSetters() {
        Client client = new Client();
        client.setId(UUID.randomUUID());

        LoyaltyTransaction tx = new LoyaltyTransaction(client, 10, "Rendez-vous honoré");
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();

        tx.setId(id);
        tx.setCreatedAt(createdAt);

        assertEquals(id, tx.getId());
        assertEquals(client, tx.getClient());
        assertEquals(10, tx.getPoints());
        assertEquals("Rendez-vous honoré", tx.getDescription());
        assertEquals(createdAt, tx.getCreatedAt());
    }
}
