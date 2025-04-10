package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleCategoryDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleCategoryService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
public class VehicleCategoryController {

    private final VehicleCategoryService vehicleCategoryService;

    /**
     * Récupère toutes les catégories de véhicule actives.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des catégories de véhicule actives
     */
    @GetMapping
    public ResponseEntity<List<VehicleCategoryDTO>> getAllActiveVehicleCategories(@RequestParam UUID organizationId) {
        List<VehicleCategoryDTO> categories = vehicleCategoryService.getAllActiveVehicleCategories(organizationId);
        return ResponseEntity.ok(categories);
    }

    /**
     * Récupère toutes les catégories de véhicule (actives et inactives).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des catégories de véhicule
     */
    @GetMapping("/all")
    public ResponseEntity<List<VehicleCategoryDTO>> getAllVehicleCategories(@RequestParam UUID organizationId) {
        List<VehicleCategoryDTO> categories = vehicleCategoryService.getAllVehicleCategories(organizationId);
        return ResponseEntity.ok(categories);
    }

    /**
     * Récupère une catégorie de véhicule par son ID.
     *
     * @param id             L'ID de la catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La catégorie de véhicule trouvée, ou 404 si non trouvée
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleCategoryDTO> getVehicleCategoryById(@PathVariable UUID id, @RequestParam UUID organizationId) {
        return vehicleCategoryService.getVehicleCategoryById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère une catégorie de véhicule par son code.
     *
     * @param code           Le code de la catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La catégorie de véhicule trouvée, ou 404 si non trouvée
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<VehicleCategoryDTO> getVehicleCategoryByCode(@PathVariable String code, @RequestParam UUID organizationId) {
        return vehicleCategoryService.getVehicleCategoryByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée une nouvelle catégorie de véhicule.
     *
     * @param vehicleCategory La catégorie de véhicule à créer
     * @param organizationId  L'ID de l'organisation
     * @return La catégorie de véhicule créée
     */
    @PostMapping
    public ResponseEntity<VehicleCategoryDTO> createVehicleCategory(@RequestBody VehicleCategory vehicleCategory, @RequestParam UUID organizationId) {
        VehicleCategoryDTO createdCategory = vehicleCategoryService.createVehicleCategory(vehicleCategory, organizationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    /**
     * Met à jour une catégorie de véhicule.
     *
     * @param id              L'ID de la catégorie de véhicule
     * @param vehicleCategory La catégorie de véhicule mise à jour
     * @param organizationId  L'ID de l'organisation
     * @return La catégorie de véhicule mise à jour, ou 404 si non trouvée
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleCategoryDTO> updateVehicleCategory(@PathVariable UUID id, @RequestBody VehicleCategory vehicleCategory, @RequestParam UUID organizationId) {
        return vehicleCategoryService.updateVehicleCategory(id, vehicleCategory, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime une catégorie de véhicule.
     *
     * @param id             L'ID de la catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return 204 si la suppression a réussi, 404 si non trouvée
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleCategory(@PathVariable UUID id, @RequestParam UUID organizationId) {
        boolean deleted = vehicleCategoryService.deleteVehicleCategory(id, organizationId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
