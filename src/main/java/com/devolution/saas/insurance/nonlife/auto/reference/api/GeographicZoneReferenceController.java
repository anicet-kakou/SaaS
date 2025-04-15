package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.abstracts.TenantAwareCrudController;
import com.devolution.saas.common.exception.ResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.GeographicZoneDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.GeographicZoneService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.GeographicZone;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des zones géographiques.
 */
// @RestController - Disabled to avoid ambiguous mapping issues
// @RequestMapping("/api/v1/auto/reference/geographic-zones")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Geographic Zones", description = "API pour la gestion des zones géographiques (Deprecated)")
public class GeographicZoneReferenceController extends TenantAwareCrudController<GeographicZoneDTO, GeographicZone, GeographicZone> {

    private final GeographicZoneService geographicZoneService;

    @Override
    protected List<GeographicZoneDTO> listActive(UUID organizationId) {
        return geographicZoneService.getAllActiveGeographicZones(organizationId);
    }

    @Override
    protected List<GeographicZoneDTO> list(UUID organizationId) {
        return geographicZoneService.getAllGeographicZones(organizationId);
    }

    @Override
    protected GeographicZoneDTO get(UUID id, UUID organizationId) {
        return geographicZoneService.getGeographicZoneById(id, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Zone géographique", id));
    }

    @Override
    protected GeographicZoneDTO getByCode(String code, UUID organizationId) {
        return geographicZoneService.getByCode(code, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forCode("Zone géographique", code));
    }

    @Override
    protected GeographicZoneDTO create(GeographicZone command, UUID organizationId) {
        return geographicZoneService.createGeographicZone(command, organizationId);
    }

    @Override
    protected GeographicZoneDTO update(UUID id, GeographicZone command, UUID organizationId) {
        return geographicZoneService.updateGeographicZone(id, command, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Zone géographique", id));
    }

    @Override
    protected GeographicZoneDTO setActive(UUID id, boolean active, UUID organizationId) {
        return geographicZoneService.setActive(id, active, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Zone géographique", id));
    }

    @Override
    protected void delete(UUID id, UUID organizationId) {
        boolean deleted = geographicZoneService.delete(id, organizationId);
        if (!deleted) {
            throw ResourceNotFoundException.forId("Zone géographique", id);
        }
    }

    @Override
    protected String getEntityName() {
        return "zone géographique";
    }


}
