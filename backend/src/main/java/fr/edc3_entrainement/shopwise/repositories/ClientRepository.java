package fr.edc3_entrainement.shopwise.repositories;

import fr.edc3_entrainement.shopwise.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
	java.util.Optional<Client> findByEmail(String email);
}
