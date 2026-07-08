package fr.edc3_entrainement.shopwise.service;

import fr.edc3_entrainement.shopwise.controller.dto.CreateRendezVousDto;
import fr.edc3_entrainement.shopwise.controller.dto.RendezVousDto;
import fr.edc3_entrainement.shopwise.controller.dto.UpdateRendezVousStatusDto;
import fr.edc3_entrainement.shopwise.models.RendezVousStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RendezVousService {
    RendezVousDto createRendezVous(CreateRendezVousDto createRendezVousDto);

    List<RendezVousDto> getRendezVous(LocalDate date, RendezVousStatus statut, UUID clientId);

    RendezVousDto updateRendezVousStatus(UUID id, UpdateRendezVousStatusDto updateStatusDto);
}
