package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.abstracts.AbstractReferenceController;
import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.CirculationZoneDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.CirculationZoneService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.CirculationZone;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Tag(name = "Circulation Zones", description = "API pour la gestion des zones de circulation")
public class CirculationZoneReferenceController extends AbstractReferenceController<CirculationZoneDTO, CirculationZone, CirculationZone> {

    private final CirculationZoneService circulationZoneService;

    @Override
    protected List<CirculationZoneDTO> getAllActive(UUID organizationId) {
        return circulationZoneService.getAllActiveCirculationZones(organizationId);
    }

    @Override
    protected CirculationZoneDTO getById(UUID id, UUID organizationId) {
        return circulationZoneService.getCirculationZoneById(id, organizationId)
                .orElseThrow(() -> new RuntimeException("Zone de circulation non trouvée avec ID: " + id));
    }

    @Override
    protected CirculationZoneDTO create(CirculationZone command, UUID organizationId) {
        return circulationZoneService.createCirculationZone(command, organizationId);
    }

    @Override
    protected CirculationZoneDTO update(UUID id, CirculationZone command, UUID organizationId) {
        return circulationZoneService.updateCirculationZone(id, command, organizationId)
                .orElseThrow(() -> new RuntimeException("Zone de circulation non trouvée avec ID: " + id));
    }

    @Override
    protected CirculationZoneDTO setActive(UUID id, boolean active, UUID organizationId) {
        return circulationZoneService.setCirculationZoneActive(id, active, organizationId)
                .orElseThrow(() -> new RuntimeException("Zone de circulation non trouvée avec ID: " + id));
    }

    @Override
    protected String getEntityName() {
        return "zone de circulation";
    }

    /**
     * Récupère toutes les zones de circulation (actives et inactives).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des zones de circulation
     */
    @GetMapping("/all")
    @Operation(summary = "Récupère toutes les zones de circulation (actives et inactives)")
    @Auditable(action = "API_GET_ALL_CIRCULATION_ZONES")
    @TenantRequired
    public ResponseEntity<List<CirculationZoneDTO>> getAllCirculationZones(@RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer toutes les zones de circulation pour l'organisation: {}", organizationId);
        List<CirculationZoneDTO> zones = circulationZoneService.getAllCirculationZones(organizationId);
        return ResponseEntity.ok(zones);
    }

    /**
     * Récupère une zone de circulation par son code.
     *
     * @param code           Le code de la zone de circulation
     * @param organizationId L'ID de l'organisation
     * @return La zone de circulation trouvée, ou 404 si non trouvée
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "Récupère une zone de circulation par son code")
    @Auditable(action = "API_GET_CIRCULATION_ZONE_BY_CODE")
    @TenantRequired
    public ResponseEntity<CirculationZoneDTO> getCirculationZoneByCode(
            @PathVariable String code,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer la zone de circulation avec code: {} pour l'organisation: {}",
                code, organizationId);
        return circulationZoneService.getCirculationZoneByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
