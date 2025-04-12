package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.abstracts.AbstractReferenceController;
import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleFuelTypeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleFuelTypeService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleFuelType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des types de carburant de véhicule.
 */
@RestController
@RequestMapping("/api/v1/auto/reference/vehicle-fuel-types")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Fuel Types", description = "API pour la gestion des types de carburant de véhicule")
public class VehicleFuelTypeReferenceController extends AbstractReferenceController<VehicleFuelTypeDTO, VehicleFuelType, VehicleFuelType> {

    private final VehicleFuelTypeService vehicleFuelTypeService;

    @Override
    protected List<VehicleFuelTypeDTO> getAllActive(UUID organizationId) {
        return vehicleFuelTypeService.getAllActiveVehicleFuelTypes(organizationId);
    }

    @Override
    protected VehicleFuelTypeDTO getById(UUID id, UUID organizationId) {
        return vehicleFuelTypeService.getVehicleFuelTypeById(id, organizationId)
                .orElseThrow(() -> new RuntimeException("Type de carburant non trouvé avec ID: " + id));
    }

    @Override
    protected VehicleFuelTypeDTO create(VehicleFuelType command, UUID organizationId) {
        return vehicleFuelTypeService.createVehicleFuelType(command, organizationId);
    }

    @Override
    protected VehicleFuelTypeDTO update(UUID id, VehicleFuelType command, UUID organizationId) {
        return vehicleFuelTypeService.updateVehicleFuelType(id, command, organizationId)
                .orElseThrow(() -> new RuntimeException("Type de carburant non trouvé avec ID: " + id));
    }

    @Override
    protected VehicleFuelTypeDTO setActive(UUID id, boolean active, UUID organizationId) {
        // Cette méthode n'est pas implémentée dans le service existant
        // Nous devons l'implémenter ou lancer une exception
        throw new UnsupportedOperationException("Méthode non implémentée");
    }

    @Override
    protected String getEntityName() {
        return "type de carburant";
    }

    /**
     * Récupère tous les types de carburant de véhicule (actifs et inactifs).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carburant de véhicule
     */
    @GetMapping("/all")
    @Operation(summary = "Récupère tous les types de carburant de véhicule (actifs et inactifs)")
    @Auditable(action = "API_GET_ALL_VEHICLE_FUEL_TYPES")
    @TenantRequired
    public ResponseEntity<List<VehicleFuelTypeDTO>> getAllVehicleFuelTypes(@RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer tous les types de carburant de véhicule pour l'organisation: {}", organizationId);
        List<VehicleFuelTypeDTO> fuelTypes = vehicleFuelTypeService.getAllVehicleFuelTypes(organizationId);
        return ResponseEntity.ok(fuelTypes);
    }

    /**
     * Récupère un type de carburant de véhicule par son code.
     *
     * @param code           Le code du type de carburant de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "Récupère un type de carburant de véhicule par son code")
    @Auditable(action = "API_GET_VEHICLE_FUEL_TYPE_BY_CODE")
    @TenantRequired
    public ResponseEntity<VehicleFuelTypeDTO> getVehicleFuelTypeByCode(
            @PathVariable String code,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer le type de carburant de véhicule avec code: {} pour l'organisation: {}",
                code, organizationId);
        return vehicleFuelTypeService.getVehicleFuelTypeByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
