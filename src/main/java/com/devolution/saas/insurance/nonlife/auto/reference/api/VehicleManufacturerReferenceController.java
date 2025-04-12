package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.abstracts.AbstractReferenceController;
import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleManufacturerDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleManufacturerService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleManufacturer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des fabricants de véhicule.
 */
@RestController
@RequestMapping("/api/v1/auto/reference/vehicle-manufacturers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Manufacturers", description = "API pour la gestion des fabricants de véhicule")
public class VehicleManufacturerReferenceController extends AbstractReferenceController<VehicleManufacturerDTO, VehicleManufacturer, VehicleManufacturer> {

    private final VehicleManufacturerService vehicleManufacturerService;

    @Override
    protected List<VehicleManufacturerDTO> getAllActive(UUID organizationId) {
        return vehicleManufacturerService.getAllActiveVehicleManufacturers(organizationId);
    }

    @Override
    protected VehicleManufacturerDTO getById(UUID id, UUID organizationId) {
        return vehicleManufacturerService.getVehicleManufacturerById(id, organizationId)
                .orElseThrow(() -> new RuntimeException("Fabricant de véhicule non trouvé avec ID: " + id));
    }

    @Override
    protected VehicleManufacturerDTO create(VehicleManufacturer command, UUID organizationId) {
        return vehicleManufacturerService.createVehicleManufacturer(command, organizationId);
    }

    @Override
    protected VehicleManufacturerDTO update(UUID id, VehicleManufacturer command, UUID organizationId) {
        return vehicleManufacturerService.updateVehicleManufacturer(id, command, organizationId)
                .orElseThrow(() -> new RuntimeException("Fabricant de véhicule non trouvé avec ID: " + id));
    }

    @Override
    protected VehicleManufacturerDTO setActive(UUID id, boolean active, UUID organizationId) {
        // Cette méthode n'est pas implémentée dans le service existant
        // Nous devons l'implémenter ou lancer une exception
        throw new UnsupportedOperationException("Méthode non implémentée");
    }

    @Override
    protected String getEntityName() {
        return "fabricant de véhicule";
    }

    /**
     * Récupère tous les fabricants de véhicule (actifs et inactifs).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des fabricants de véhicule
     */
    @GetMapping("/all")
    @Operation(summary = "Récupère tous les fabricants de véhicule (actifs et inactifs)")
    @Auditable(action = "API_GET_ALL_VEHICLE_MANUFACTURERS")
    @TenantRequired
    public ResponseEntity<List<VehicleManufacturerDTO>> getAllVehicleManufacturers(@RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer tous les fabricants de véhicule pour l'organisation: {}", organizationId);
        List<VehicleManufacturerDTO> manufacturers = vehicleManufacturerService.getAllVehicleManufacturers(organizationId);
        return ResponseEntity.ok(manufacturers);
    }

    /**
     * Récupère un fabricant de véhicule par son code.
     *
     * @param code           Le code du fabricant de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le fabricant de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "Récupère un fabricant de véhicule par son code")
    @Auditable(action = "API_GET_VEHICLE_MANUFACTURER_BY_CODE")
    @TenantRequired
    public ResponseEntity<VehicleManufacturerDTO> getVehicleManufacturerByCode(
            @PathVariable String code,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer le fabricant de véhicule avec code: {} pour l'organisation: {}",
                code, organizationId);
        return vehicleManufacturerService.getVehicleManufacturerByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
