package fr.edc3_entrainement.shopwise.service.impl;

import fr.edc3_entrainement.shopwise.controller.dto.CreateRendezVousDto;
import fr.edc3_entrainement.shopwise.controller.dto.RendezVousDto;
import fr.edc3_entrainement.shopwise.controller.dto.UpdateRendezVousStatusDto;
import fr.edc3_entrainement.shopwise.models.Client;
import fr.edc3_entrainement.shopwise.models.RendezVous;
import fr.edc3_entrainement.shopwise.models.RendezVousStatus;
import fr.edc3_entrainement.shopwise.repositories.ClientRepository;
import fr.edc3_entrainement.shopwise.repositories.RendezVousRepository;
import fr.edc3_entrainement.shopwise.service.RendezVousService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RendezVousServiceImpl implements RendezVousService {

    private static final int POINTS_PER_HONORED_APPOINTMENT = 10;

    private final RendezVousRepository rendezVousRepository;
    private final ClientRepository clientRepository;

    public RendezVousServiceImpl(RendezVousRepository rendezVousRepository, ClientRepository clientRepository) {
        this.rendezVousRepository = rendezVousRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public RendezVousDto createRendezVous(CreateRendezVousDto createRendezVousDto) {
        Client client = clientRepository.findById(createRendezVousDto.getClientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        RendezVous rendezVous = new RendezVous();
        rendezVous.setClient(client);
        rendezVous.setDateRendezVous(createRendezVousDto.getDateRendezVous());
        rendezVous.setService(createRendezVousDto.getService());
        rendezVous.setStatut(RendezVousStatus.PLANIFIE);

        RendezVous savedRendezVous = rendezVousRepository.save(rendezVous);
        return toDto(savedRendezVous);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RendezVousDto> getRendezVous(LocalDate date, RendezVousStatus statut, UUID clientId) {
        boolean hasDate = date != null;
        boolean hasStatut = statut != null;
        boolean hasClient = clientId != null;

        LocalDateTime startDate = hasDate ? date.atStartOfDay() : LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime endDate = hasDate ? date.plusDays(1).atStartOfDay() : LocalDate.of(1970, 1, 2).atStartOfDay();
        RendezVousStatus effectiveStatut = hasStatut ? statut : RendezVousStatus.PLANIFIE;
        UUID effectiveClientId = hasClient ? clientId : new UUID(0L, 0L);

        return rendezVousRepository.findByOptionalFilters(
                hasDate,
                startDate,
                endDate,
                hasStatut,
                effectiveStatut,
                hasClient,
                effectiveClientId
            ).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional
    public RendezVousDto updateRendezVousStatus(UUID id, UpdateRendezVousStatusDto updateStatusDto) {
        RendezVous rendezVous = rendezVousRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RendezVous not found"));

        RendezVousStatus previousStatus = rendezVous.getStatut();
        RendezVousStatus newStatus = updateStatusDto.getStatut();

        if (previousStatus == newStatus) {
            return toDto(rendezVous);
        }

        rendezVous.setStatut(newStatus);

        if (previousStatus != RendezVousStatus.HONORE && newStatus == RendezVousStatus.HONORE) {
            Client client = rendezVous.getClient();
            client.setPointsFidelite(client.getPointsFidelite() + POINTS_PER_HONORED_APPOINTMENT);
        }

        RendezVous updatedRendezVous = rendezVousRepository.save(rendezVous);
        return toDto(updatedRendezVous);
    }

    private RendezVousDto toDto(RendezVous rendezVous) {
        return new RendezVousDto(
                rendezVous.getId(),
                rendezVous.getDateRendezVous(),
                rendezVous.getService(),
                rendezVous.getStatut().name(),
                rendezVous.getClient().getId(),
                rendezVous.getClient().getNom()
        );
    }
}
