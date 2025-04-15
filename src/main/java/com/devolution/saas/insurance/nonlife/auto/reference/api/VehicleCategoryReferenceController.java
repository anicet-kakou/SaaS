package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.abstracts.TenantAwareCrudController;
import com.devolution.saas.common.exception.ResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleCategoryDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleCategoryService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleCategory;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des catégories de véhicule.
 */
// @RestController - Disabled to avoid ambiguous mapping issues
// @RequestMapping("/api/v1/auto/reference/vehicle-categories")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Categories", description = "API pour la gestion des catégories de véhicule (Deprecated)")
public class VehicleCategoryReferenceController extends TenantAwareCrudController<VehicleCategoryDTO, VehicleCategory, VehicleCategory> {

    private final VehicleCategoryService vehicleCategoryService;

    @Override
    protected List<VehicleCategoryDTO> listActive(UUID organizationId) {
        return vehicleCategoryService.getAllActiveVehicleCategories(organizationId);
    }

    @Override
    protected List<VehicleCategoryDTO> list(UUID organizationId) {
        return vehicleCategoryService.getAllVehicleCategories(organizationId);
    }

    @Override
    protected VehicleCategoryDTO get(UUID id, UUID organizationId) {
        return vehicleCategoryService.getVehicleCategoryById(id, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Catégorie de véhicule", id));
    }

    @Override
    protected VehicleCategoryDTO getByCode(String code, UUID organizationId) {
        return vehicleCategoryService.getVehicleCategoryByCode(code, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forCode("Catégorie de véhicule", code));
    }

    @Override
    protected VehicleCategoryDTO create(VehicleCategory command, UUID organizationId) {
        return vehicleCategoryService.createVehicleCategory(command, organizationId);
    }

    @Override
    protected VehicleCategoryDTO update(UUID id, VehicleCategory command, UUID organizationId) {
        return vehicleCategoryService.updateVehicleCategory(id, command, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Catégorie de véhicule", id));
    }

    @Override
    protected VehicleCategoryDTO setActive(UUID id, boolean active, UUID organizationId) {
        return vehicleCategoryService.setActive(id, active, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Catégorie de véhicule", id));
    }

    @Override
    protected void delete(UUID id, UUID organizationId) {
        boolean deleted = vehicleCategoryService.deleteVehicleCategory(id, organizationId);
        if (!deleted) {
            throw ResourceNotFoundException.forId("Catégorie de véhicule", id);
        }
    }

    @Override
    protected String getEntityName() {
        return "catégorie de véhicule";
    }


}
