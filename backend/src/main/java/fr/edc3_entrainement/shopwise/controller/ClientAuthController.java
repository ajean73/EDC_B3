package fr.edc3_entrainement.shopwise.controller;

import fr.edc3_entrainement.shopwise.models.Client;
import fr.edc3_entrainement.shopwise.models.LoyaltyTransaction;
import fr.edc3_entrainement.shopwise.repositories.LoyaltyTransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class ClientAuthController {

    private final LoyaltyTransactionRepository loyaltyTransactionRepository;
    private final fr.edc3_entrainement.shopwise.repositories.ClientRepository clientRepository;

    public ClientAuthController(LoyaltyTransactionRepository loyaltyTransactionRepository, fr.edc3_entrainement.shopwise.repositories.ClientRepository clientRepository) {
        this.loyaltyTransactionRepository = loyaltyTransactionRepository;
        this.clientRepository = clientRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClient(@org.springframework.web.bind.annotation.PathVariable java.util.UUID id) {
        Client client = clientRepository.findById(id).orElse(null);
        if (client == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(new java.util.HashMap<String, Object>() {{
            put("id", client.getId());
            put("nom", client.getNom());
            put("email", client.getEmail());
            put("telephone", client.getTelephone());
            put("pointsFidelite", client.getPointsFidelite());
        }});
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<?>> transactions(@org.springframework.web.bind.annotation.PathVariable java.util.UUID id) {
        Client client = clientRepository.findById(id).orElse(null);
        if (client == null) return ResponseEntity.notFound().build();

        List<LoyaltyTransaction> txs = loyaltyTransactionRepository.findByClientIdOrderByCreatedAtDesc(client.getId());
        List<Object> dtos = txs.stream().map(t -> new java.util.HashMap<String,Object>() {{
            put("id", t.getId());
            put("points", t.getPoints());
            put("description", t.getDescription());
            put("createdAt", t.getCreatedAt());
        }}).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}
