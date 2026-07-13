package fr.edc3_entrainement.shopwise.dto;

import fr.edc3_entrainement.shopwise.controller.dto.AuthResponseDto;
import fr.edc3_entrainement.shopwise.controller.dto.CreateRendezVousDto;
import fr.edc3_entrainement.shopwise.controller.dto.LoginRequestDto;
import fr.edc3_entrainement.shopwise.controller.dto.RendezVousDto;
import fr.edc3_entrainement.shopwise.controller.dto.UpdateRendezVousStatusDto;
import fr.edc3_entrainement.shopwise.models.RendezVousStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DtoTest {

    @Test
    @DisplayName("doit manipuler CreateRendezVousDto")
    void createRendezVousDto_gettersSetters() {
        CreateRendezVousDto dto = new CreateRendezVousDto();
        UUID clientId = UUID.randomUUID();
        LocalDateTime date = LocalDateTime.now().plusDays(1);

        dto.setClientId(clientId);
        dto.setDateRendezVous(date);
        dto.setService("Conseil");

        assertEquals(clientId, dto.getClientId());
        assertEquals(date, dto.getDateRendezVous());
        assertEquals("Conseil", dto.getService());
    }

    @Test
    @DisplayName("doit manipuler UpdateRendezVousStatusDto")
    void updateStatusDto_gettersSetters() {
        UpdateRendezVousStatusDto dto = new UpdateRendezVousStatusDto();
        dto.setStatut(RendezVousStatus.ANNULE);
        assertEquals(RendezVousStatus.ANNULE, dto.getStatut());
    }

    @Test
    @DisplayName("doit manipuler LoginRequestDto et AuthResponseDto")
    void authDtos_gettersSetters() {
        LoginRequestDto login = new LoginRequestDto();
        login.setEmail("a@a.com");
        login.setPassword("pwd");

        AuthResponseDto auth = new AuthResponseDto("token-1");

        assertEquals("a@a.com", login.getEmail());
        assertEquals("pwd", login.getPassword());
        assertEquals("token-1", auth.getToken());

        auth.setToken("token-2");
        assertEquals("token-2", auth.getToken());
    }

    @Test
    @DisplayName("doit manipuler RendezVousDto")
    void rendezVousDto_gettersSetters() {
        UUID id = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        LocalDateTime date = LocalDateTime.now();

        RendezVousDto dto = new RendezVousDto(id, date, "Atelier", "PLANIFIE", clientId, "Alice");

        assertEquals(id, dto.getId());
        assertEquals(date, dto.getDateRendezVous());
        assertEquals("Atelier", dto.getService());
        assertEquals("PLANIFIE", dto.getStatut());
        assertEquals(clientId, dto.getClientId());
        assertEquals("Alice", dto.getClientNom());

        dto.setService("Conseil");
        dto.setStatut("HONORE");
        assertEquals("Conseil", dto.getService());
        assertEquals("HONORE", dto.getStatut());
    }
}
