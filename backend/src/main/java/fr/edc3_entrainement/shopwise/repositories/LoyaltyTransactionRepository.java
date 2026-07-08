package fr.edc3_entrainement.shopwise.repositories;

import fr.edc3_entrainement.shopwise.models.LoyaltyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoyaltyTransactionRepository extends JpaRepository<LoyaltyTransaction, UUID> {
    List<LoyaltyTransaction> findByClientIdOrderByCreatedAtDesc(java.util.UUID clientId);
}
