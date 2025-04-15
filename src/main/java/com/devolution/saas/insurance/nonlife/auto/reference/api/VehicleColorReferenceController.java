package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.abstracts.TenantAwareCrudController;
import com.devolution.saas.common.exception.ResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleColorDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleColorService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleColor;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des couleurs de véhicule.
 */
// @RestController - Disabled to avoid ambiguous mapping issues
// @RequestMapping("/api/v1/auto/reference/vehicle-colors")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Colors", description = "API pour la gestion des couleurs de véhicule (Deprecated)")
public class VehicleColorReferenceController extends TenantAwareCrudController<VehicleColorDTO, VehicleColor, VehicleColor> {

    private final VehicleColorService vehicleColorService;

    @Override
    protected List<VehicleColorDTO> listActive(UUID organizationId) {
        return vehicleColorService.getAllActiveVehicleColors(organizationId);
    }

    @Override
    protected List<VehicleColorDTO> list(UUID organizationId) {
        return vehicleColorService.getAllVehicleColors(organizationId);
    }

    @Override
    protected VehicleColorDTO get(UUID id, UUID organizationId) {
        return vehicleColorService.getVehicleColorById(id, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Couleur de véhicule", id));
    }

    @Override
    protected VehicleColorDTO getByCode(String code, UUID organizationId) {
        return vehicleColorService.getVehicleColorByCode(code, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forCode("Couleur de véhicule", code));
    }

    @Override
    protected VehicleColorDTO create(VehicleColor command, UUID organizationId) {
        return vehicleColorService.createVehicleColor(command, organizationId);
    }

    @Override
    protected VehicleColorDTO update(UUID id, VehicleColor command, UUID organizationId) {
        return vehicleColorService.updateVehicleColor(id, command, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Couleur de véhicule", id));
    }

    @Override
    protected VehicleColorDTO setActive(UUID id, boolean active, UUID organizationId) {
        return vehicleColorService.setActive(id, active, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Couleur de véhicule", id));
    }

    @Override
    protected void delete(UUID id, UUID organizationId) {
        boolean deleted = vehicleColorService.deleteVehicleColor(id, organizationId);
        if (!deleted) {
            throw ResourceNotFoundException.forId("Couleur de véhicule", id);
        }
    }

    @Override
    protected String getEntityName() {
        return "couleur de véhicule";
    }


}
