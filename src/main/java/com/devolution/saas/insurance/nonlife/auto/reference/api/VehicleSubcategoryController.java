package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleSubcategoryDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleSubcategoryService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleSubcategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des sous-catégories de véhicule.
 */
@RestController
@RequestMapping("/api/v1/auto/reference/vehicle-subcategories-management")
@RequiredArgsConstructor
public class VehicleSubcategoryController {

    private final VehicleSubcategoryService vehicleSubcategoryService;

    /**
     * Récupère toutes les sous-catégories de véhicule actives.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des sous-catégories de véhicule actives
     */
    @GetMapping
    public ResponseEntity<List<VehicleSubcategoryDTO>> getAllActiveVehicleSubcategories(@RequestParam UUID organizationId) {
        List<VehicleSubcategoryDTO> subcategories = vehicleSubcategoryService.getAllActiveVehicleSubcategories(organizationId);
        return ResponseEntity.ok(subcategories);
    }

    /**
     * Récupère toutes les sous-catégories de véhicule (actives et inactives).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des sous-catégories de véhicule
     */
    @GetMapping("/all")
    public ResponseEntity<List<VehicleSubcategoryDTO>> getAllVehicleSubcategories(@RequestParam UUID organizationId) {
        List<VehicleSubcategoryDTO> subcategories = vehicleSubcategoryService.getAllVehicleSubcategories(organizationId);
        return ResponseEntity.ok(subcategories);
    }

    /**
     * Récupère toutes les sous-catégories de véhicule actives pour une catégorie donnée.
     *
     * @param categoryId     L'ID de la catégorie
     * @param organizationId L'ID de l'organisation
     * @return La liste des sous-catégories de véhicule actives pour la catégorie
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<VehicleSubcategoryDTO>> getAllActiveVehicleSubcategoriesByCategory(@PathVariable UUID categoryId, @RequestParam UUID organizationId) {
        List<VehicleSubcategoryDTO> subcategories = vehicleSubcategoryService.getAllActiveVehicleSubcategoriesByCategory(categoryId, organizationId);
        return ResponseEntity.ok(subcategories);
    }

    /**
     * Récupère toutes les sous-catégories de véhicule (actives et inactives) pour une catégorie donnée.
     *
     * @param categoryId     L'ID de la catégorie
     * @param organizationId L'ID de l'organisation
     * @return La liste des sous-catégories de véhicule pour la catégorie
     */
    @GetMapping("/category/{categoryId}/all")
    public ResponseEntity<List<VehicleSubcategoryDTO>> getAllVehicleSubcategoriesByCategory(@PathVariable UUID categoryId, @RequestParam UUID organizationId) {
        List<VehicleSubcategoryDTO> subcategories = vehicleSubcategoryService.getAllVehicleSubcategoriesByCategory(categoryId, organizationId);
        return ResponseEntity.ok(subcategories);
    }

    /**
     * Récupère une sous-catégorie de véhicule par son ID.
     *
     * @param id             L'ID de la sous-catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La sous-catégorie de véhicule trouvée, ou 404 si non trouvée
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleSubcategoryDTO> getVehicleSubcategoryById(@PathVariable UUID id, @RequestParam UUID organizationId) {
        return vehicleSubcategoryService.getVehicleSubcategoryById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère une sous-catégorie de véhicule par son code.
     *
     * @param code           Le code de la sous-catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La sous-catégorie de véhicule trouvée, ou 404 si non trouvée
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<VehicleSubcategoryDTO> getVehicleSubcategoryByCode(@PathVariable String code, @RequestParam UUID organizationId) {
        return vehicleSubcategoryService.getVehicleSubcategoryByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée une nouvelle sous-catégorie de véhicule.
     *
     * @param vehicleSubcategory La sous-catégorie de véhicule à créer
     * @param organizationId     L'ID de l'organisation
     * @return La sous-catégorie de véhicule créée
     */
    @PostMapping
    public ResponseEntity<VehicleSubcategoryDTO> createVehicleSubcategory(@RequestBody VehicleSubcategory vehicleSubcategory, @RequestParam UUID organizationId) {
        VehicleSubcategoryDTO createdSubcategory = vehicleSubcategoryService.createVehicleSubcategory(vehicleSubcategory, organizationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubcategory);
    }

    /**
     * Met à jour une sous-catégorie de véhicule.
     *
     * @param id                 L'ID de la sous-catégorie de véhicule
     * @param vehicleSubcategory La sous-catégorie de véhicule mise à jour
     * @param organizationId     L'ID de l'organisation
     * @return La sous-catégorie de véhicule mise à jour, ou 404 si non trouvée
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleSubcategoryDTO> updateVehicleSubcategory(@PathVariable UUID id, @RequestBody VehicleSubcategory vehicleSubcategory, @RequestParam UUID organizationId) {
        return vehicleSubcategoryService.updateVehicleSubcategory(id, vehicleSubcategory, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime une sous-catégorie de véhicule.
     *
     * @param id             L'ID de la sous-catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return 204 si la suppression a réussi, 404 si non trouvée
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleSubcategory(@PathVariable UUID id, @RequestParam UUID organizationId) {
        boolean deleted = vehicleSubcategoryService.deleteVehicleSubcategory(id, organizationId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
