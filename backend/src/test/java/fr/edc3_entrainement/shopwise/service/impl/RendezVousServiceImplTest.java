package fr.edc3_entrainement.shopwise.service.impl;

import fr.edc3_entrainement.shopwise.controller.dto.UpdateRendezVousStatusDto;
import fr.edc3_entrainement.shopwise.models.Client;
import fr.edc3_entrainement.shopwise.models.LoyaltyTransaction;
import fr.edc3_entrainement.shopwise.models.RendezVous;
import fr.edc3_entrainement.shopwise.models.RendezVousStatus;
import fr.edc3_entrainement.shopwise.repositories.ClientRepository;
import fr.edc3_entrainement.shopwise.repositories.LoyaltyTransactionRepository;
import fr.edc3_entrainement.shopwise.repositories.RendezVousRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RendezVousServiceImplTest {

    @Mock
    private RendezVousRepository rendezVousRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private LoyaltyTransactionRepository loyaltyTransactionRepository;

    @InjectMocks
    private RendezVousServiceImpl rendezVousService;

    private Client client;
    private RendezVous rendezVous;
    private UUID rendezVousId;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(UUID.randomUUID());
        client.setNom("Client Test");
        client.setPointsFidelite(0);

        rendezVousId = UUID.randomUUID();
        rendezVous = new RendezVous();
        rendezVous.setId(rendezVousId);
        rendezVous.setClient(client);
        rendezVous.setService("Coupe");
        rendezVous.setDateRendezVous(LocalDateTime.of(2026, 7, 9, 10, 0));
        rendezVous.setStatut(RendezVousStatus.PLANIFIE);
    }

    @Test
    @DisplayName("doit ajouter des points et creer une transaction quand un rendez-vous passe a HONORE")
    void updateStatus_toHonore_addsPointsAndCreatesTransaction() {
        UpdateRendezVousStatusDto dto = new UpdateRendezVousStatusDto();
        dto.setStatut(RendezVousStatus.HONORE);

        when(rendezVousRepository.findById(rendezVousId)).thenReturn(Optional.of(rendezVous));
        when(rendezVousRepository.save(any(RendezVous.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = rendezVousService.updateRendezVousStatus(rendezVousId, dto);

        assertEquals("HONORE", result.getStatut());
        assertEquals(10, client.getPointsFidelite());
        verify(loyaltyTransactionRepository).save(any(LoyaltyTransaction.class));
    }

    @Test
    @DisplayName("ne doit pas ajouter de points ni de transaction si le statut ne change pas")
    void updateStatus_toSameStatus_doesNotAddPointsOrTransaction() {
        rendezVous.setStatut(RendezVousStatus.HONORE);
        client.setPointsFidelite(30);

        UpdateRendezVousStatusDto dto = new UpdateRendezVousStatusDto();
        dto.setStatut(RendezVousStatus.HONORE);

        when(rendezVousRepository.findById(rendezVousId)).thenReturn(Optional.of(rendezVous));

        var result = rendezVousService.updateRendezVousStatus(rendezVousId, dto);

        assertEquals("HONORE", result.getStatut());
        assertEquals(30, client.getPointsFidelite());
        verify(loyaltyTransactionRepository, never()).save(any(LoyaltyTransaction.class));
        verify(rendezVousRepository, never()).save(any(RendezVous.class));
    }

    @Test
    @DisplayName("doit transmettre correctement les filtres de recherche au repository")
    void getRendezVous_forwardsFilterFlagsAndDatesToRepository() {
        LocalDate date = LocalDate.of(2026, 7, 9);
        UUID clientId = UUID.randomUUID();

        when(rendezVousRepository.findByOptionalFilters(any(Boolean.class), any(LocalDateTime.class), any(LocalDateTime.class), any(Boolean.class), any(RendezVousStatus.class), any(Boolean.class), any(UUID.class)))
                .thenReturn(List.of(rendezVous));

        var results = rendezVousService.getRendezVous(date, RendezVousStatus.PLANIFIE, clientId);

        assertEquals(1, results.size());

        ArgumentCaptor<Boolean> hasDateCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<LocalDateTime> startCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> endCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<Boolean> hasStatutCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<RendezVousStatus> statutCaptor = ArgumentCaptor.forClass(RendezVousStatus.class);
        ArgumentCaptor<Boolean> hasClientCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<UUID> clientCaptor = ArgumentCaptor.forClass(UUID.class);

        verify(rendezVousRepository).findByOptionalFilters(
                hasDateCaptor.capture(),
                startCaptor.capture(),
                endCaptor.capture(),
                hasStatutCaptor.capture(),
                statutCaptor.capture(),
                hasClientCaptor.capture(),
                clientCaptor.capture()
        );

        assertTrue(hasDateCaptor.getValue());
        assertEquals(LocalDateTime.of(2026, 7, 9, 0, 0), startCaptor.getValue());
        assertEquals(LocalDateTime.of(2026, 7, 10, 0, 0), endCaptor.getValue());
        assertTrue(hasStatutCaptor.getValue());
        assertEquals(RendezVousStatus.PLANIFIE, statutCaptor.getValue());
        assertTrue(hasClientCaptor.getValue());
        assertEquals(clientId, clientCaptor.getValue());
    }

    @Test
    @DisplayName("doit utiliser les valeurs par defaut quand aucun filtre n est fourni")
    void getRendezVous_withoutFilters_usesDefaultValues() {
        when(rendezVousRepository.findByOptionalFilters(any(Boolean.class), any(LocalDateTime.class), any(LocalDateTime.class), any(Boolean.class), any(RendezVousStatus.class), any(Boolean.class), any(UUID.class)))
                .thenReturn(List.of());

        List<?> results = rendezVousService.getRendezVous(null, null, null);

        assertEquals(0, results.size());

        ArgumentCaptor<Boolean> hasDateCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<Boolean> hasStatutCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<Boolean> hasClientCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(rendezVousRepository).findByOptionalFilters(
                hasDateCaptor.capture(),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                hasStatutCaptor.capture(),
                any(RendezVousStatus.class),
                hasClientCaptor.capture(),
                any(UUID.class)
        );

        assertEquals(false, hasDateCaptor.getValue());
        assertEquals(false, hasStatutCaptor.getValue());
        assertEquals(false, hasClientCaptor.getValue());
    }

    @Test
    @DisplayName("doit continuer meme si l enregistrement de transaction echoue")
    void updateStatus_whenTransactionSaveFails_stillSavesRendezVous() {
        UpdateRendezVousStatusDto dto = new UpdateRendezVousStatusDto();
        dto.setStatut(RendezVousStatus.HONORE);

        when(rendezVousRepository.findById(rendezVousId)).thenReturn(Optional.of(rendezVous));
        when(rendezVousRepository.save(any(RendezVous.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doThrow(new RuntimeException("erreur technique"))
                .when(loyaltyTransactionRepository)
                .save(any(LoyaltyTransaction.class));

        var result = rendezVousService.updateRendezVousStatus(rendezVousId, dto);

        assertEquals("HONORE", result.getStatut());
        verify(rendezVousRepository).save(any(RendezVous.class));
    }
}
