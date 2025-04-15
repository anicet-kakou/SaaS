package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.abstracts.TenantAwareCrudController;
import com.devolution.saas.common.exception.ResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleGenreDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleGenreService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleGenre;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des genres de véhicule.
 */
// @RestController - Disabled to avoid ambiguous mapping issues
// @RequestMapping("/api/v1/auto/reference/vehicle-genres")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Genres", description = "API pour la gestion des genres de véhicule (Deprecated)")
public class VehicleGenreReferenceController extends TenantAwareCrudController<VehicleGenreDTO, VehicleGenre, VehicleGenre> {

    private final VehicleGenreService vehicleGenreService;

    @Override
    protected List<VehicleGenreDTO> listActive(UUID organizationId) {
        return vehicleGenreService.getAllActiveVehicleGenres(organizationId);
    }

    @Override
    protected List<VehicleGenreDTO> list(UUID organizationId) {
        return vehicleGenreService.getAllVehicleGenres(organizationId);
    }

    @Override
    protected VehicleGenreDTO get(UUID id, UUID organizationId) {
        return vehicleGenreService.getVehicleGenreById(id, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Genre de véhicule", id));
    }

    @Override
    protected VehicleGenreDTO getByCode(String code, UUID organizationId) {
        return vehicleGenreService.getVehicleGenreByCode(code, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forCode("Genre de véhicule", code));
    }

    @Override
    protected VehicleGenreDTO create(VehicleGenre command, UUID organizationId) {
        return vehicleGenreService.createVehicleGenre(command, organizationId);
    }

    @Override
    protected VehicleGenreDTO update(UUID id, VehicleGenre command, UUID organizationId) {
        return vehicleGenreService.updateVehicleGenre(id, command, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Genre de véhicule", id));
    }

    @Override
    protected VehicleGenreDTO setActive(UUID id, boolean active, UUID organizationId) {
        return vehicleGenreService.setActive(id, active, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Genre de véhicule", id));
    }

    @Override
    protected void delete(UUID id, UUID organizationId) {
        boolean deleted = vehicleGenreService.deleteVehicleGenre(id, organizationId);
        if (!deleted) {
            throw ResourceNotFoundException.forId("Genre de véhicule", id);
        }
    }

    @Override
    protected String getEntityName() {
        return "genre de véhicule";
    }


}
