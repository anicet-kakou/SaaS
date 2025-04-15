package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.abstracts.TenantAwareCrudController;
import com.devolution.saas.common.exception.ResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.CirculationZoneDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.CirculationZoneService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.CirculationZone;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des zones de circulation.
 */
// @RestController - Disabled to avoid ambiguous mapping issues
// @RequestMapping("/api/v1/auto/reference/circulation-zones")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Circulation Zones", description = "API pour la gestion des zones de circulation (Deprecated)")
public class CirculationZoneReferenceController extends TenantAwareCrudController<CirculationZoneDTO, CirculationZone, CirculationZone> {

    private final CirculationZoneService circulationZoneService;

    @Override
    protected List<CirculationZoneDTO> listActive(UUID organizationId) {
        return circulationZoneService.getAllActiveCirculationZones(organizationId);
    }

    @Override
    protected List<CirculationZoneDTO> list(UUID organizationId) {
        return circulationZoneService.getAllCirculationZones(organizationId);
    }

    @Override
    protected CirculationZoneDTO get(UUID id, UUID organizationId) {
        return circulationZoneService.getCirculationZoneById(id, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Zone de circulation", id));
    }

    @Override
    protected CirculationZoneDTO getByCode(String code, UUID organizationId) {
        return circulationZoneService.getByCode(code, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forCode("Zone de circulation", code));
    }

    @Override
    protected CirculationZoneDTO create(CirculationZone command, UUID organizationId) {
        return circulationZoneService.createCirculationZone(command, organizationId);
    }

    @Override
    protected CirculationZoneDTO update(UUID id, CirculationZone command, UUID organizationId) {
        return circulationZoneService.updateCirculationZone(id, command, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Zone de circulation", id));
    }

    @Override
    protected CirculationZoneDTO setActive(UUID id, boolean active, UUID organizationId) {
        return circulationZoneService.setActive(id, active, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Zone de circulation", id));
    }

    @Override
    protected void delete(UUID id, UUID organizationId) {
        boolean deleted = circulationZoneService.delete(id, organizationId);
        if (!deleted) {
            throw ResourceNotFoundException.forId("Zone de circulation", id);
        }
    }

    @Override
    protected String getEntityName() {
        return "zone de circulation";
    }

    /**
     * Override the getEntity method from AbstractCrudController to prevent ambiguous mapping.
     * This method should never be called directly as we're using the tenant-aware version.
     *
     * @param id ID of the entity
     * @return Never returns as it throws an exception
     * @throws UnsupportedOperationException Always thrown to prevent usage
     */
    @Override
    @GetMapping("/legacy/{id}")
    public ResponseEntity<CirculationZoneDTO> getEntity(@PathVariable UUID id) {
        throw new UnsupportedOperationException("Cette méthode ne devrait pas être appelée directement. Utilisez getEntity(id, organizationId) à la place.");
    }
}
