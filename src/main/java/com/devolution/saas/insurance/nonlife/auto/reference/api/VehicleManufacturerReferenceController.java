package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.abstracts.TenantAwareCrudController;
import com.devolution.saas.common.exception.ResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleManufacturerDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleManufacturerService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleManufacturer;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des fabricants de véhicule.
 */
// @RestController - Disabled to avoid ambiguous mapping issues
// @RequestMapping("/api/v1/auto/reference/vehicle-manufacturers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Manufacturers", description = "API pour la gestion des fabricants de véhicule (Deprecated)")
public class VehicleManufacturerReferenceController extends TenantAwareCrudController<VehicleManufacturerDTO, VehicleManufacturer, VehicleManufacturer> {

    private final VehicleManufacturerService vehicleManufacturerService;

    @Override
    protected List<VehicleManufacturerDTO> listActive(UUID organizationId) {
        return vehicleManufacturerService.getAllActiveVehicleManufacturers(organizationId);
    }

    @Override
    protected List<VehicleManufacturerDTO> list(UUID organizationId) {
        return vehicleManufacturerService.getAllVehicleManufacturers(organizationId);
    }

    @Override
    protected VehicleManufacturerDTO get(UUID id, UUID organizationId) {
        return vehicleManufacturerService.getVehicleManufacturerById(id, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Fabricant de véhicule", id));
    }

    @Override
    protected VehicleManufacturerDTO getByCode(String code, UUID organizationId) {
        return vehicleManufacturerService.getVehicleManufacturerByCode(code, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forCode("Fabricant de véhicule", code));
    }

    @Override
    protected VehicleManufacturerDTO create(VehicleManufacturer command, UUID organizationId) {
        return vehicleManufacturerService.createVehicleManufacturer(command, organizationId);
    }

    @Override
    protected VehicleManufacturerDTO update(UUID id, VehicleManufacturer command, UUID organizationId) {
        return vehicleManufacturerService.updateVehicleManufacturer(id, command, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Fabricant de véhicule", id));
    }

    @Override
    protected VehicleManufacturerDTO setActive(UUID id, boolean active, UUID organizationId) {
        return vehicleManufacturerService.setActive(id, active, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Fabricant de véhicule", id));
    }

    @Override
    protected void delete(UUID id, UUID organizationId) {
        boolean deleted = vehicleManufacturerService.deleteVehicleManufacturer(id, organizationId);
        if (!deleted) {
            throw ResourceNotFoundException.forId("Fabricant de véhicule", id);
        }
    }

    @Override
    protected String getEntityName() {
        return "fabricant de véhicule";
    }


}
