package fr.edc3_entrainement.shopwise.repositories;

import fr.edc3_entrainement.shopwise.models.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RendezVousRepository extends JpaRepository<RendezVous, UUID> {
}