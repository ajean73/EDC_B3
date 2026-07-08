package fr.edc3_entrainement.shopwise.controller;

import fr.edc3_entrainement.shopwise.controller.dto.CreateRendezVousDto;
import fr.edc3_entrainement.shopwise.controller.dto.RendezVousDto;
import fr.edc3_entrainement.shopwise.controller.dto.UpdateRendezVousStatusDto;
import fr.edc3_entrainement.shopwise.service.RendezVousService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rendezvous")
public class RendezVousController {

    private final RendezVousService rendezVousService;

    public RendezVousController(RendezVousService rendezVousService) {
        this.rendezVousService = rendezVousService;
    }

    @PostMapping
    public ResponseEntity<RendezVousDto> createRendezVous(@Valid @RequestBody CreateRendezVousDto createRendezVousDto) {
        RendezVousDto createdRendezVous = rendezVousService.createRendezVous(createRendezVousDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRendezVous);
    }

    @GetMapping
    public List<RendezVousDto> getAllRendezVous() {
        return rendezVousService.getAllRendezVous();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RendezVousDto> updateRendezVousStatus(@PathVariable UUID id, @Valid @RequestBody UpdateRendezVousStatusDto payload) {
        RendezVousDto updatedRendezVous = rendezVousService.updateRendezVousStatus(id, payload);
        return ResponseEntity.ok(updatedRendezVous);
    }
}