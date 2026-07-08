package fr.edc3_entrainement.shopwise.controller.dto;

import fr.edc3_entrainement.shopwise.models.RendezVousStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateRendezVousStatusDto {
    @NotNull(message = "Le statut est obligatoire.")
    private RendezVousStatus statut;

    public RendezVousStatus getStatut() { return statut; }
    public void setStatut(RendezVousStatus statut) { this.statut = statut; }
}
