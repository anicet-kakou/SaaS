package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.CirculationZoneDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.CirculationZoneService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.CirculationZone;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des zones de circulation.
 */
@RestController
@RequestMapping("/api/v1/auto/reference/circulation-zones")
@RequiredArgsConstructor
public class CirculationZoneController {

    private final CirculationZoneService circulationZoneService;

    /**
     * Récupère toutes les zones de circulation actives.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des zones de circulation actives
     */
    @GetMapping
    public ResponseEntity<List<CirculationZoneDTO>> getAllActiveCirculationZones(@RequestParam UUID organizationId) {
        List<CirculationZoneDTO> zones = circulationZoneService.getAllActiveCirculationZones(organizationId);
        return ResponseEntity.ok(zones);
    }

    /**
     * Récupère toutes les zones de circulation (actives et inactives).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des zones de circulation
     */
    @GetMapping("/all")
    public ResponseEntity<List<CirculationZoneDTO>> getAllCirculationZones(@RequestParam UUID organizationId) {
        List<CirculationZoneDTO> zones = circulationZoneService.getAllCirculationZones(organizationId);
        return ResponseEntity.ok(zones);
    }

    /**
     * Récupère une zone de circulation par son ID.
     *
     * @param id             L'ID de la zone de circulation
     * @param organizationId L'ID de l'organisation
     * @return La zone de circulation trouvée, ou 404 si non trouvée
     */
    @GetMapping("/{id}")
    public ResponseEntity<CirculationZoneDTO> getCirculationZoneById(@PathVariable UUID id, @RequestParam UUID organizationId) {
        return circulationZoneService.getCirculationZoneById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère une zone de circulation par son code.
     *
     * @param code           Le code de la zone de circulation
     * @param organizationId L'ID de l'organisation
     * @return La zone de circulation trouvée, ou 404 si non trouvée
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<CirculationZoneDTO> getCirculationZoneByCode(@PathVariable String code, @RequestParam UUID organizationId) {
        return circulationZoneService.getCirculationZoneByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée une nouvelle zone de circulation.
     *
     * @param circulationZone La zone de circulation à créer
     * @param organizationId  L'ID de l'organisation
     * @return La zone de circulation créée
     */
    @PostMapping
    public ResponseEntity<CirculationZoneDTO> createCirculationZone(@RequestBody CirculationZone circulationZone, @RequestParam UUID organizationId) {
        CirculationZoneDTO createdZone = circulationZoneService.createCirculationZone(circulationZone, organizationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdZone);
    }

    /**
     * Met à jour une zone de circulation.
     *
     * @param id              L'ID de la zone de circulation
     * @param circulationZone La zone de circulation mise à jour
     * @param organizationId  L'ID de l'organisation
     * @return La zone de circulation mise à jour, ou 404 si non trouvée
     */
    @PutMapping("/{id}")
    public ResponseEntity<CirculationZoneDTO> updateCirculationZone(@PathVariable UUID id, @RequestBody CirculationZone circulationZone, @RequestParam UUID organizationId) {
        return circulationZoneService.updateCirculationZone(id, circulationZone, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime une zone de circulation.
     *
     * @param id             L'ID de la zone de circulation
     * @param organizationId L'ID de l'organisation
     * @return 204 si la suppression a réussi, 404 si non trouvée
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCirculationZone(@PathVariable UUID id, @RequestParam UUID organizationId) {
        boolean deleted = circulationZoneService.deleteCirculationZone(id, organizationId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
