package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.GeographicZoneDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.GeographicZoneService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.GeographicZone;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des zones géographiques.
 *
 * @deprecated Utiliser {@link GeographicZoneReferenceController} à la place.
 * Ce contrôleur sera supprimé dans une version future.
 */
@Deprecated(since = "1.0.0", forRemoval = true)
@RestController
@RequestMapping("/api/v1/auto/reference/geographic-zones-management")
@RequiredArgsConstructor
public class GeographicZoneController {

    private final GeographicZoneService geographicZoneService;

    /**
     * Récupère toutes les zones géographiques actives.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des zones géographiques actives
     */
    @GetMapping
    public ResponseEntity<List<GeographicZoneDTO>> getAllActiveGeographicZones(@RequestParam UUID organizationId) {
        List<GeographicZoneDTO> zones = geographicZoneService.getAllActiveGeographicZones(organizationId);
        return ResponseEntity.ok(zones);
    }

    /**
     * Récupère toutes les zones géographiques (actives et inactives).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des zones géographiques
     */
    @GetMapping("/all")
    public ResponseEntity<List<GeographicZoneDTO>> getAllGeographicZones(@RequestParam UUID organizationId) {
        List<GeographicZoneDTO> zones = geographicZoneService.getAllGeographicZones(organizationId);
        return ResponseEntity.ok(zones);
    }

    /**
     * Récupère une zone géographique par son ID.
     *
     * @param id             L'ID de la zone géographique
     * @param organizationId L'ID de l'organisation
     * @return La zone géographique trouvée, ou 404 si non trouvée
     */
    @GetMapping("/{id}")
    public ResponseEntity<GeographicZoneDTO> getGeographicZoneById(@PathVariable UUID id, @RequestParam UUID organizationId) {
        return geographicZoneService.getGeographicZoneById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère une zone géographique par son code.
     *
     * @param code           Le code de la zone géographique
     * @param organizationId L'ID de l'organisation
     * @return La zone géographique trouvée, ou 404 si non trouvée
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<GeographicZoneDTO> getGeographicZoneByCode(@PathVariable String code, @RequestParam UUID organizationId) {
        return geographicZoneService.getGeographicZoneByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée une nouvelle zone géographique.
     *
     * @param geographicZone La zone géographique à créer
     * @param organizationId L'ID de l'organisation
     * @return La zone géographique créée
     */
    @PostMapping
    public ResponseEntity<GeographicZoneDTO> createGeographicZone(@RequestBody GeographicZone geographicZone, @RequestParam UUID organizationId) {
        GeographicZoneDTO createdZone = geographicZoneService.createGeographicZone(geographicZone, organizationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdZone);
    }

    /**
     * Met à jour une zone géographique.
     *
     * @param id             L'ID de la zone géographique
     * @param geographicZone La zone géographique mise à jour
     * @param organizationId L'ID de l'organisation
     * @return La zone géographique mise à jour, ou 404 si non trouvée
     */
    @PutMapping("/{id}")
    public ResponseEntity<GeographicZoneDTO> updateGeographicZone(@PathVariable UUID id, @RequestBody GeographicZone geographicZone, @RequestParam UUID organizationId) {
        return geographicZoneService.updateGeographicZone(id, geographicZone, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime une zone géographique.
     *
     * @param id             L'ID de la zone géographique
     * @param organizationId L'ID de l'organisation
     * @return 204 si la suppression a réussi, 404 si non trouvée
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGeographicZone(@PathVariable UUID id, @RequestParam UUID organizationId) {
        boolean deleted = geographicZoneService.deleteGeographicZone(id, organizationId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
