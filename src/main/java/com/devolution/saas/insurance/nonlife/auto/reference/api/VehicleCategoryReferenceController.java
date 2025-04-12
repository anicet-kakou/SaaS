package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.abstracts.AbstractReferenceController;
import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleCategoryDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleCategoryService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des catégories de véhicule.
 */
@RestController
@RequestMapping("/api/v1/auto/reference/vehicle-categories")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Categories", description = "API pour la gestion des catégories de véhicule")
public class VehicleCategoryReferenceController extends AbstractReferenceController<VehicleCategoryDTO, VehicleCategory, VehicleCategory> {

    private final VehicleCategoryService vehicleCategoryService;

    @Override
    protected List<VehicleCategoryDTO> getAllActive(UUID organizationId) {
        return vehicleCategoryService.getAllActiveVehicleCategories(organizationId);
    }

    @Override
    protected VehicleCategoryDTO getById(UUID id, UUID organizationId) {
        return vehicleCategoryService.getVehicleCategoryById(id, organizationId)
                .orElseThrow(() -> new RuntimeException("Catégorie de véhicule non trouvée avec ID: " + id));
    }

    @Override
    protected VehicleCategoryDTO create(VehicleCategory command, UUID organizationId) {
        return vehicleCategoryService.createVehicleCategory(command, organizationId);
    }

    @Override
    protected VehicleCategoryDTO update(UUID id, VehicleCategory command, UUID organizationId) {
        return vehicleCategoryService.updateVehicleCategory(id, command, organizationId)
                .orElseThrow(() -> new RuntimeException("Catégorie de véhicule non trouvée avec ID: " + id));
    }

    @Override
    protected VehicleCategoryDTO setActive(UUID id, boolean active, UUID organizationId) {
        // Cette méthode n'est pas implémentée dans le service existant
        // Nous devons l'implémenter ou lancer une exception
        throw new UnsupportedOperationException("Méthode non implémentée");
    }

    @Override
    protected String getEntityName() {
        return "catégorie de véhicule";
    }

    /**
     * Récupère toutes les catégories de véhicule (actives et inactives).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des catégories de véhicule
     */
    @GetMapping("/all")
    @Operation(summary = "Récupère toutes les catégories de véhicule (actives et inactives)")
    @Auditable(action = "API_GET_ALL_VEHICLE_CATEGORIES")
    @TenantRequired
    public ResponseEntity<List<VehicleCategoryDTO>> getAllVehicleCategories(@RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer toutes les catégories de véhicule pour l'organisation: {}", organizationId);
        List<VehicleCategoryDTO> categories = vehicleCategoryService.getAllVehicleCategories(organizationId);
        return ResponseEntity.ok(categories);
    }

    /**
     * Récupère une catégorie de véhicule par son code.
     *
     * @param code           Le code de la catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La catégorie de véhicule trouvée, ou 404 si non trouvée
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "Récupère une catégorie de véhicule par son code")
    @Auditable(action = "API_GET_VEHICLE_CATEGORY_BY_CODE")
    @TenantRequired
    public ResponseEntity<VehicleCategoryDTO> getVehicleCategoryByCode(
            @PathVariable String code,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer la catégorie de véhicule avec code: {} pour l'organisation: {}",
                code, organizationId);
        return vehicleCategoryService.getVehicleCategoryByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
