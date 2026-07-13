package fr.edc3_entrainement.shopwise.controller;

import fr.edc3_entrainement.shopwise.models.Client;
import fr.edc3_entrainement.shopwise.models.LoyaltyTransaction;
import fr.edc3_entrainement.shopwise.repositories.ClientRepository;
import fr.edc3_entrainement.shopwise.repositories.LoyaltyTransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientAuthControllerTest {

    @Mock
    private LoyaltyTransactionRepository loyaltyTransactionRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientAuthController clientAuthController;

    @Test
    @DisplayName("doit retourner 404 si le client n existe pas")
    void getClient_notFound() {
        UUID id = UUID.randomUUID();
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<?> response = clientAuthController.getClient(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("doit retourner les informations du client")
    void getClient_found() {
        UUID id = UUID.randomUUID();
        Client client = new Client();
        client.setId(id);
        client.setNom("Alice");
        client.setEmail("a@a.com");
        client.setTelephone("0102030405");
        client.setPointsFidelite(15);

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));

        ResponseEntity<?> response = clientAuthController.getClient(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<?, ?> payload = (Map<?, ?>) response.getBody();
        assertNotNull(payload);
        assertEquals("Alice", payload.get("nom"));
        assertEquals(15, payload.get("pointsFidelite"));
    }

    @Test
    @DisplayName("doit retourner 404 pour les transactions si client absent")
    void transactions_notFound() {
        UUID id = UUID.randomUUID();
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<List<?>> response = clientAuthController.transactions(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("doit retourner les transactions du client")
    void transactions_found() {
        UUID id = UUID.randomUUID();
        Client client = new Client();
        client.setId(id);

        LoyaltyTransaction tx = new LoyaltyTransaction();
        tx.setId(UUID.randomUUID());
        tx.setPoints(10);
        tx.setDescription("Rendez-vous honoré");
        tx.setCreatedAt(LocalDateTime.now());

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
        when(loyaltyTransactionRepository.findByClientIdOrderByCreatedAtDesc(id)).thenReturn(List.of(tx));

        ResponseEntity<List<?>> response = clientAuthController.transactions(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
}
