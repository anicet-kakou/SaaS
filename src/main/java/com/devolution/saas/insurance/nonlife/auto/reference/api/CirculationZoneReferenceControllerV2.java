package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.common.exception.ResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.CirculationZoneDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.CirculationZoneService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.CirculationZone;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des zones de circulation.
 * Version 2 qui ne dépend pas de l'héritage problématique.
 */
@RestController
@RequestMapping("/api/v2/auto/reference/circulation-zones")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Circulation Zones V2", description = "API pour la gestion des zones de circulation (V2)")
public class CirculationZoneReferenceControllerV2 {

    private final CirculationZoneService circulationZoneService;

    /**
     * Liste toutes les zones de circulation actives.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des zones de circulation actives
     */
    @GetMapping
    @Operation(summary = "Liste toutes les zones de circulation actives")
    @Auditable(action = "API_LIST_ACTIVE")
    @TenantRequired
    public ResponseEntity<List<CirculationZoneDTO>> listActiveEntities(@RequestParam UUID organizationId) {
        log.debug("REST request pour lister les zones de circulation actives pour l'organisation: {}", organizationId);
        List<CirculationZoneDTO> result = circulationZoneService.getAllActiveCirculationZones(organizationId);
        return ResponseEntity.ok(result);
    }

    /**
     * Liste toutes les zones de circulation (actives et inactives).
     *
     * @param organizationId ID de l'organisation
     * @return Liste des zones de circulation
     */
    @GetMapping("/all")
    @Operation(summary = "Liste toutes les zones de circulation (actives et inactives)")
    @Auditable(action = "API_LIST_ALL")
    @TenantRequired
    public ResponseEntity<List<CirculationZoneDTO>> listAllEntities(@RequestParam UUID organizationId) {
        log.debug("REST request pour lister toutes les zones de circulation pour l'organisation: {}", organizationId);
        List<CirculationZoneDTO> result = circulationZoneService.getAllCirculationZones(organizationId);
        return ResponseEntity.ok(result);
    }

    /**
     * Récupère une zone de circulation par son ID.
     *
     * @param id             ID de la zone de circulation
     * @param organizationId ID de l'organisation
     * @return Zone de circulation
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupère une zone de circulation par son ID")
    @Auditable(action = "API_GET")
    @TenantRequired
    public ResponseEntity<CirculationZoneDTO> getEntity(@PathVariable UUID id, @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer la zone de circulation avec ID: {} pour l'organisation: {}", id, organizationId);
        CirculationZoneDTO result = circulationZoneService.getCirculationZoneById(id, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Zone de circulation", id));
        return ResponseEntity.ok(result);
    }

    /**
     * Récupère une zone de circulation par son code.
     *
     * @param code           Code de la zone de circulation
     * @param organizationId ID de l'organisation
     * @return Zone de circulation
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "Récupère une zone de circulation par son code")
    @Auditable(action = "API_GET_BY_CODE")
    @TenantRequired
    public ResponseEntity<CirculationZoneDTO> getEntityByCode(@PathVariable String code, @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer la zone de circulation avec code: {} pour l'organisation: {}", code, organizationId);
        CirculationZoneDTO result = circulationZoneService.getByCode(code, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forCode("Zone de circulation", code));
        return ResponseEntity.ok(result);
    }

    /**
     * Crée une nouvelle zone de circulation.
     *
     * @param command        Zone de circulation à créer
     * @param organizationId ID de l'organisation
     * @return Zone de circulation créée
     */
    @PostMapping
    @Operation(summary = "Crée une nouvelle zone de circulation")
    @Auditable(action = "API_CREATE")
    @TenantRequired
    public ResponseEntity<CirculationZoneDTO> createEntity(@Valid @RequestBody CirculationZone command, @RequestParam UUID organizationId) {
        log.debug("REST request pour créer une zone de circulation pour l'organisation: {}", organizationId);
        CirculationZoneDTO result = circulationZoneService.createCirculationZone(command, organizationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Met à jour une zone de circulation existante.
     *
     * @param id             ID de la zone de circulation
     * @param command        Zone de circulation à mettre à jour
     * @param organizationId ID de l'organisation
     * @return Zone de circulation mise à jour
     */
    @PutMapping("/{id}")
    @Operation(summary = "Met à jour une zone de circulation existante")
    @Auditable(action = "API_UPDATE")
    @TenantRequired
    public ResponseEntity<CirculationZoneDTO> updateEntity(@PathVariable UUID id, @Valid @RequestBody CirculationZone command, @RequestParam UUID organizationId) {
        log.debug("REST request pour mettre à jour la zone de circulation avec ID: {} pour l'organisation: {}", id, organizationId);
        CirculationZoneDTO result = circulationZoneService.updateCirculationZone(id, command, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Zone de circulation", id));
        return ResponseEntity.ok(result);
    }

    /**
     * Active une zone de circulation.
     *
     * @param id             ID de la zone de circulation
     * @param organizationId ID de l'organisation
     * @return Zone de circulation activée
     */
    @PutMapping("/{id}/activate")
    @Operation(summary = "Active une zone de circulation")
    @Auditable(action = "API_ACTIVATE")
    @TenantRequired
    public ResponseEntity<CirculationZoneDTO> activateEntity(@PathVariable UUID id, @RequestParam UUID organizationId) {
        log.debug("REST request pour activer la zone de circulation avec ID: {} pour l'organisation: {}", id, organizationId);
        CirculationZoneDTO result = circulationZoneService.setActive(id, true, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Zone de circulation", id));
        return ResponseEntity.ok(result);
    }

    /**
     * Désactive une zone de circulation.
     *
     * @param id             ID de la zone de circulation
     * @param organizationId ID de l'organisation
     * @return Zone de circulation désactivée
     */
    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Désactive une zone de circulation")
    @Auditable(action = "API_DEACTIVATE")
    @TenantRequired
    public ResponseEntity<CirculationZoneDTO> deactivateEntity(@PathVariable UUID id, @RequestParam UUID organizationId) {
        log.debug("REST request pour désactiver la zone de circulation avec ID: {} pour l'organisation: {}", id, organizationId);
        CirculationZoneDTO result = circulationZoneService.setActive(id, false, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Zone de circulation", id));
        return ResponseEntity.ok(result);
    }

    /**
     * Supprime une zone de circulation.
     *
     * @param id             ID de la zone de circulation
     * @param organizationId ID de l'organisation
     * @return Réponse HTTP vide
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime une zone de circulation")
    @Auditable(action = "API_DELETE")
    @TenantRequired
    public ResponseEntity<Void> deleteEntity(@PathVariable UUID id, @RequestParam UUID organizationId) {
        log.debug("REST request pour supprimer la zone de circulation avec ID: {} pour l'organisation: {}", id, organizationId);
        boolean deleted = circulationZoneService.delete(id, organizationId);
        if (!deleted) {
            throw ResourceNotFoundException.forId("Zone de circulation", id);
        }
        return ResponseEntity.noContent().build();
    }
}
