package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.abstracts.TenantAwareCrudController;
import com.devolution.saas.common.exception.ResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleUsageDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleUsageService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleUsage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des types d'usage de véhicule.
 */
// @RestController - Disabled to avoid ambiguous mapping issues
// @RequestMapping("/api/v1/auto/reference/vehicle-usages")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Usages", description = "API pour la gestion des types d'usage de véhicule (Deprecated)")
public class VehicleUsageReferenceController extends TenantAwareCrudController<VehicleUsageDTO, VehicleUsage, VehicleUsage> {

    private final VehicleUsageService vehicleUsageService;

    @Override
    protected List<VehicleUsageDTO> listActive(UUID organizationId) {
        return vehicleUsageService.getAllActiveVehicleUsages(organizationId);
    }

    @Override
    protected List<VehicleUsageDTO> list(UUID organizationId) {
        return vehicleUsageService.getAllVehicleUsages(organizationId);
    }

    @Override
    protected VehicleUsageDTO get(UUID id, UUID organizationId) {
        return vehicleUsageService.getVehicleUsageById(id, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Type d'usage de véhicule", id));
    }

    @Override
    protected VehicleUsageDTO getByCode(String code, UUID organizationId) {
        return vehicleUsageService.getVehicleUsageByCode(code, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forCode("Type d'usage de véhicule", code));
    }

    @Override
    protected VehicleUsageDTO create(VehicleUsage command, UUID organizationId) {
        return vehicleUsageService.createVehicleUsage(command, organizationId);
    }

    @Override
    protected VehicleUsageDTO update(UUID id, VehicleUsage command, UUID organizationId) {
        return vehicleUsageService.updateVehicleUsage(id, command, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Type d'usage de véhicule", id));
    }

    @Override
    protected VehicleUsageDTO setActive(UUID id, boolean active, UUID organizationId) {
        return vehicleUsageService.setActive(id, active, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Type d'usage de véhicule", id));
    }

    @Override
    protected void delete(UUID id, UUID organizationId) {
        boolean deleted = vehicleUsageService.deleteVehicleUsage(id, organizationId);
        if (!deleted) {
            throw ResourceNotFoundException.forId("Type d'usage de véhicule", id);
        }
    }

    @Override
    protected String getEntityName() {
        return "type d'usage de véhicule";
    }


}
