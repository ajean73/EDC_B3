package fr.edc3_entrainement.shopwise.controller;

import fr.edc3_entrainement.shopwise.models.Client;
import fr.edc3_entrainement.shopwise.repositories.ClientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientController clientController;

    @Test
    @DisplayName("doit retourner tous les clients")
    void getAllClients_retourneListe() {
        Client c1 = new Client();
        c1.setNom("Alice");
        when(clientRepository.findAll()).thenReturn(List.of(c1));

        List<Client> clients = clientController.getAllClients();

        assertEquals(1, clients.size());
        assertEquals("Alice", clients.get(0).getNom());
    }

    @Test
    @DisplayName("doit creer un client")
    void createClient_persisteClient() {
        Client input = new Client();
        input.setNom("Bob");
        when(clientRepository.save(input)).thenReturn(input);

        Client saved = clientController.createClient(input);

        assertEquals("Bob", saved.getNom());
        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(captor.capture());
        assertEquals("Bob", captor.getValue().getNom());
    }
}
