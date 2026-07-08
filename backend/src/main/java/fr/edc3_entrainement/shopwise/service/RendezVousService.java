package fr.edc3_entrainement.shopwise.service;

import fr.edc3_entrainement.shopwise.controller.dto.CreateRendezVousDto;
import fr.edc3_entrainement.shopwise.controller.dto.RendezVousDto;
import fr.edc3_entrainement.shopwise.controller.dto.UpdateRendezVousStatusDto;

import java.util.List;
import java.util.UUID;

public interface RendezVousService {
    RendezVousDto createRendezVous(CreateRendezVousDto createRendezVousDto);

    List<RendezVousDto> getAllRendezVous();

    RendezVousDto updateRendezVousStatus(UUID id, UpdateRendezVousStatusDto updateStatusDto);
}
