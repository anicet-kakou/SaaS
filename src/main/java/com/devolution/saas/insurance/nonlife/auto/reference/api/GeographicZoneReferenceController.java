package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.abstracts.AbstractReferenceController;
import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.GeographicZoneDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.GeographicZoneService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.GeographicZone;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des zones géographiques.
 */
@RestController
@RequestMapping("/api/v1/auto/reference/geographic-zones")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Geographic Zones", description = "API pour la gestion des zones géographiques")
public class GeographicZoneReferenceController extends AbstractReferenceController<GeographicZoneDTO, GeographicZone, GeographicZone> {

    private final GeographicZoneService geographicZoneService;

    @Override
    protected List<GeographicZoneDTO> getAllActive(UUID organizationId) {
        return geographicZoneService.getAllActiveGeographicZones(organizationId);
    }

    @Override
    protected GeographicZoneDTO getById(UUID id, UUID organizationId) {
        return geographicZoneService.getGeographicZoneById(id, organizationId)
                .orElseThrow(() -> new RuntimeException("Zone géographique non trouvée avec ID: " + id));
    }

    @Override
    protected GeographicZoneDTO create(GeographicZone command, UUID organizationId) {
        return geographicZoneService.createGeographicZone(command, organizationId);
    }

    @Override
    protected GeographicZoneDTO update(UUID id, GeographicZone command, UUID organizationId) {
        return geographicZoneService.updateGeographicZone(id, command, organizationId)
                .orElseThrow(() -> new RuntimeException("Zone géographique non trouvée avec ID: " + id));
    }

    @Override
    protected GeographicZoneDTO setActive(UUID id, boolean active, UUID organizationId) {
        return geographicZoneService.setGeographicZoneActive(id, active, organizationId)
                .orElseThrow(() -> new RuntimeException("Zone géographique non trouvée avec ID: " + id));
    }

    @Override
    protected String getEntityName() {
        return "zone géographique";
    }

    /**
     * Récupère toutes les zones géographiques (actives et inactives).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des zones géographiques
     */
    @GetMapping("/all")
    @Operation(summary = "Récupère toutes les zones géographiques (actives et inactives)")
    @Auditable(action = "API_GET_ALL_GEOGRAPHIC_ZONES")
    @TenantRequired
    public ResponseEntity<List<GeographicZoneDTO>> getAllGeographicZones(@RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer toutes les zones géographiques pour l'organisation: {}", organizationId);
        List<GeographicZoneDTO> zones = geographicZoneService.getAllGeographicZones(organizationId);
        return ResponseEntity.ok(zones);
    }

    /**
     * Récupère une zone géographique par son code.
     *
     * @param code           Le code de la zone géographique
     * @param organizationId L'ID de l'organisation
     * @return La zone géographique trouvée, ou 404 si non trouvée
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "Récupère une zone géographique par son code")
    @Auditable(action = "API_GET_GEOGRAPHIC_ZONE_BY_CODE")
    @TenantRequired
    public ResponseEntity<GeographicZoneDTO> getGeographicZoneByCode(
            @PathVariable String code,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer la zone géographique avec code: {} pour l'organisation: {}",
                code, organizationId);
        return geographicZoneService.getGeographicZoneByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
