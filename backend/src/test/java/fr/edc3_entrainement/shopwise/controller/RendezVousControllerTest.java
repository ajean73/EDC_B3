package fr.edc3_entrainement.shopwise.controller;

import fr.edc3_entrainement.shopwise.controller.dto.CreateRendezVousDto;
import fr.edc3_entrainement.shopwise.controller.dto.RendezVousDto;
import fr.edc3_entrainement.shopwise.controller.dto.UpdateRendezVousStatusDto;
import fr.edc3_entrainement.shopwise.models.RendezVousStatus;
import fr.edc3_entrainement.shopwise.service.RendezVousService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RendezVousControllerTest {

    @Mock
    private RendezVousService rendezVousService;

    @InjectMocks
    private RendezVousController rendezVousController;

    @Test
    @DisplayName("doit creer un rendez-vous")
    void createRendezVous_ok() {
        CreateRendezVousDto input = new CreateRendezVousDto();
        input.setClientId(UUID.randomUUID());
        input.setDateRendezVous(LocalDateTime.now().plusDays(1));
        input.setService("Conseil");

        RendezVousDto dto = new RendezVousDto(
                UUID.randomUUID(),
                input.getDateRendezVous(),
                "Conseil",
                "PLANIFIE",
                input.getClientId(),
                "Alice"
        );

        when(rendezVousService.createRendezVous(input)).thenReturn(dto);

        ResponseEntity<RendezVousDto> response = rendezVousController.createRendezVous(input);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("PLANIFIE", response.getBody().getStatut());
    }

    @Test
    @DisplayName("doit retourner les rendez-vous filtres")
    void getRendezVous_ok() {
        LocalDate date = LocalDate.of(2026, 7, 13);
        UUID clientId = UUID.randomUUID();

        when(rendezVousService.getRendezVous(date, RendezVousStatus.PLANIFIE, clientId))
                .thenReturn(List.of());

        List<RendezVousDto> result = rendezVousController.getRendezVous(date, RendezVousStatus.PLANIFIE, clientId);

        assertEquals(0, result.size());
        verify(rendezVousService).getRendezVous(date, RendezVousStatus.PLANIFIE, clientId);
    }

    @Test
    @DisplayName("doit mettre a jour le statut d un rendez-vous")
    void updateStatus_ok() {
        UUID id = UUID.randomUUID();
        UpdateRendezVousStatusDto payload = new UpdateRendezVousStatusDto();
        payload.setStatut(RendezVousStatus.HONORE);

        RendezVousDto dto = new RendezVousDto(
                id,
                LocalDateTime.now(),
                "Atelier",
                "HONORE",
                UUID.randomUUID(),
                "Bob"
        );

        when(rendezVousService.updateRendezVousStatus(id, payload)).thenReturn(dto);

        ResponseEntity<RendezVousDto> response = rendezVousController.updateRendezVousStatus(id, payload);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("HONORE", response.getBody().getStatut());
    }
}
