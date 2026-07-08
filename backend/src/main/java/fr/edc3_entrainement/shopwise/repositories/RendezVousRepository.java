package fr.edc3_entrainement.shopwise.repositories;

import fr.edc3_entrainement.shopwise.models.RendezVous;
import fr.edc3_entrainement.shopwise.models.RendezVousStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RendezVousRepository extends JpaRepository<RendezVous, UUID> {

    @Query("""
	    SELECT r
	    FROM RendezVous r
						WHERE (:hasDate = false OR (r.dateRendezVous >= :startDate AND r.dateRendezVous < :endDate))
							AND (:hasStatut = false OR r.statut = :statut)
							AND (:hasClient = false OR r.client.id = :clientId)
	    """)
    List<RendezVous> findByOptionalFilters(
						@Param("hasDate") boolean hasDate,
	    @Param("startDate") LocalDateTime startDate,
	    @Param("endDate") LocalDateTime endDate,
						@Param("hasStatut") boolean hasStatut,
	    @Param("statut") RendezVousStatus statut,
						@Param("hasClient") boolean hasClient,
	    @Param("clientId") UUID clientId
    );
}