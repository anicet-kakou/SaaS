package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.abstracts.AbstractReferenceController;
import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleBodyTypeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleBodyTypeService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleBodyType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des types de carrosserie de véhicule.
 */
@RestController
@RequestMapping("/api/v1/auto/reference/vehicle-body-types")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Body Types", description = "API pour la gestion des types de carrosserie de véhicule")
public class VehicleBodyTypeReferenceController extends AbstractReferenceController<VehicleBodyTypeDTO, VehicleBodyType, VehicleBodyType> {

    private final VehicleBodyTypeService vehicleBodyTypeService;

    @Override
    protected List<VehicleBodyTypeDTO> getAllActive(UUID organizationId) {
        return vehicleBodyTypeService.getAllActiveVehicleBodyTypes(organizationId);
    }

    @Override
    protected VehicleBodyTypeDTO getById(UUID id, UUID organizationId) {
        return vehicleBodyTypeService.getVehicleBodyTypeById(id, organizationId)
                .orElseThrow(() -> new RuntimeException("Type de carrosserie non trouvé avec ID: " + id));
    }

    @Override
    protected VehicleBodyTypeDTO create(VehicleBodyType command, UUID organizationId) {
        return vehicleBodyTypeService.createVehicleBodyType(command, organizationId);
    }

    @Override
    protected VehicleBodyTypeDTO update(UUID id, VehicleBodyType command, UUID organizationId) {
        return vehicleBodyTypeService.updateVehicleBodyType(id, command, organizationId)
                .orElseThrow(() -> new RuntimeException("Type de carrosserie non trouvé avec ID: " + id));
    }

    @Override
    protected VehicleBodyTypeDTO setActive(UUID id, boolean active, UUID organizationId) {
        // Cette méthode n'est pas implémentée dans le service existant
        // Nous devons l'implémenter ou lancer une exception
        throw new UnsupportedOperationException("Méthode non implémentée");
    }

    @Override
    protected String getEntityName() {
        return "type de carrosserie";
    }

    /**
     * Récupère tous les types de carrosserie de véhicule (actifs et inactifs).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carrosserie de véhicule
     */
    @GetMapping("/all")
    @Operation(summary = "Récupère tous les types de carrosserie (actifs et inactifs)")
    @Auditable(action = "API_GET_ALL_VEHICLE_BODY_TYPES")
    @TenantRequired
    public ResponseEntity<List<VehicleBodyTypeDTO>> getAllVehicleBodyTypes(@RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer tous les types de carrosserie pour l'organisation: {}", organizationId);
        List<VehicleBodyTypeDTO> bodyTypes = vehicleBodyTypeService.getAllVehicleBodyTypes(organizationId);
        return ResponseEntity.ok(bodyTypes);
    }

    /**
     * Récupère un type de carrosserie de véhicule par son code.
     *
     * @param code           Le code du type de carrosserie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type de carrosserie de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "Récupère un type de carrosserie par son code")
    @Auditable(action = "API_GET_VEHICLE_BODY_TYPE_BY_CODE")
    @TenantRequired
    public ResponseEntity<VehicleBodyTypeDTO> getVehicleBodyTypeByCode(@PathVariable String code, @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer le type de carrosserie avec code: {} pour l'organisation: {}", code, organizationId);
        return vehicleBodyTypeService.getVehicleBodyTypeByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
