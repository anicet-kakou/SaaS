package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.abstracts.TenantAwareCrudController;
import com.devolution.saas.common.exception.ResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleBodyTypeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleBodyTypeService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleBodyType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des types de carrosserie de véhicule.
 */
// @RestController - Disabled to avoid ambiguous mapping issues
// @RequestMapping("/api/v1/auto/reference/vehicle-body-types")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Body Types", description = "API pour la gestion des types de carrosserie de véhicule (Deprecated)")
public class VehicleBodyTypeReferenceController extends TenantAwareCrudController<VehicleBodyTypeDTO, VehicleBodyType, VehicleBodyType> {

    private final VehicleBodyTypeService vehicleBodyTypeService;

    @Override
    protected List<VehicleBodyTypeDTO> listActive(UUID organizationId) {
        return vehicleBodyTypeService.getAllActiveVehicleBodyTypes(organizationId);
    }

    @Override
    protected List<VehicleBodyTypeDTO> list(UUID organizationId) {
        return vehicleBodyTypeService.getAllVehicleBodyTypes(organizationId);
    }

    @Override
    protected VehicleBodyTypeDTO get(UUID id, UUID organizationId) {
        return vehicleBodyTypeService.getVehicleBodyTypeById(id, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Type de carrosserie", id));
    }

    @Override
    protected VehicleBodyTypeDTO getByCode(String code, UUID organizationId) {
        return vehicleBodyTypeService.getVehicleBodyTypeByCode(code, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forCode("Type de carrosserie", code));
    }

    @Override
    protected VehicleBodyTypeDTO create(VehicleBodyType command, UUID organizationId) {
        return vehicleBodyTypeService.createVehicleBodyType(command, organizationId);
    }

    @Override
    protected VehicleBodyTypeDTO update(UUID id, VehicleBodyType command, UUID organizationId) {
        return vehicleBodyTypeService.updateVehicleBodyType(id, command, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Type de carrosserie", id));
    }

    @Override
    protected VehicleBodyTypeDTO setActive(UUID id, boolean active, UUID organizationId) {
        return vehicleBodyTypeService.setActive(id, active, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Type de carrosserie", id));
    }

    @Override
    protected void delete(UUID id, UUID organizationId) {
        boolean deleted = vehicleBodyTypeService.deleteVehicleBodyType(id, organizationId);
        if (!deleted) {
            throw ResourceNotFoundException.forId("Type de carrosserie", id);
        }
    }

    @Override
    protected String getEntityName() {
        return "type de carrosserie";
    }


}
