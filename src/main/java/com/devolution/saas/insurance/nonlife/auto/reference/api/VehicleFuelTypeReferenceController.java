package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.abstracts.TenantAwareCrudController;
import com.devolution.saas.common.exception.ResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleFuelTypeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleFuelTypeService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleFuelType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des types de carburant de véhicule.
 */
// @RestController - Disabled to avoid ambiguous mapping issues
// @RequestMapping("/api/v1/auto/reference/vehicle-fuel-types")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Fuel Types", description = "API pour la gestion des types de carburant de véhicule (Deprecated)")
public class VehicleFuelTypeReferenceController extends TenantAwareCrudController<VehicleFuelTypeDTO, VehicleFuelType, VehicleFuelType> {

    private final VehicleFuelTypeService vehicleFuelTypeService;

    @Override
    protected List<VehicleFuelTypeDTO> listActive(UUID organizationId) {
        return vehicleFuelTypeService.getAllActiveVehicleFuelTypes(organizationId);
    }

    @Override
    protected List<VehicleFuelTypeDTO> list(UUID organizationId) {
        return vehicleFuelTypeService.getAllVehicleFuelTypes(organizationId);
    }

    @Override
    protected VehicleFuelTypeDTO get(UUID id, UUID organizationId) {
        return vehicleFuelTypeService.getVehicleFuelTypeById(id, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Type de carburant", id));
    }

    @Override
    protected VehicleFuelTypeDTO getByCode(String code, UUID organizationId) {
        return vehicleFuelTypeService.getVehicleFuelTypeByCode(code, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forCode("Type de carburant", code));
    }

    @Override
    protected VehicleFuelTypeDTO create(VehicleFuelType command, UUID organizationId) {
        return vehicleFuelTypeService.createVehicleFuelType(command, organizationId);
    }

    @Override
    protected VehicleFuelTypeDTO update(UUID id, VehicleFuelType command, UUID organizationId) {
        return vehicleFuelTypeService.updateVehicleFuelType(id, command, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Type de carburant", id));
    }

    @Override
    protected VehicleFuelTypeDTO setActive(UUID id, boolean active, UUID organizationId) {
        return vehicleFuelTypeService.setActive(id, active, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Type de carburant", id));
    }

    @Override
    protected void delete(UUID id, UUID organizationId) {
        boolean deleted = vehicleFuelTypeService.deleteVehicleFuelType(id, organizationId);
        if (!deleted) {
            throw ResourceNotFoundException.forId("Type de carburant", id);
        }
    }

    @Override
    protected String getEntityName() {
        return "type de carburant";
    }


}
