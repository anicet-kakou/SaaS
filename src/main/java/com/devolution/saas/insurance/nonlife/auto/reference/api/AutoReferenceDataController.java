package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.*;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.ReferenceDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des données de référence auto.
 */
@RestController
@RequestMapping("/api/v1/auto/reference")
@RequiredArgsConstructor
public class AutoReferenceDataController_TEMP {

    private final ReferenceDataService referenceDataService;

    /**
     * Récupère toutes les données de référence pour l'interface utilisateur.
     *
     * @param organizationId L'ID de l'organisation
     * @return Les données de référence
     */
    @GetMapping
    public ResponseEntity<Map<String, List<?>>> getReferenceData(@RequestParam UUID organizationId) {
        Map<String, List<?>> referenceData = referenceDataService.getReferenceDataForUI(organizationId);
        return ResponseEntity.ok(referenceData);
    }

    /**
     * Initialise les données de référence standard pour une nouvelle organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return Réponse vide avec statut 204
     */
    @PostMapping("/initialize")
    public ResponseEntity<Void> initializeReferenceData(@RequestParam UUID organizationId) {
        referenceDataService.initializeStandardReferenceData(organizationId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Vérifie si une référence existe.
     *
     * @param referenceType  Le type de référence
     * @param referenceId    L'ID de la référence
     * @param organizationId L'ID de l'organisation
     * @return true si la référence existe, false sinon
     */
    @GetMapping("/exists")
    public ResponseEntity<Boolean> referenceExists(@RequestParam String referenceType,
                                                   @RequestParam UUID referenceId,
                                                   @RequestParam UUID organizationId) {
        boolean exists = referenceDataService.referenceExists(referenceType, referenceId, organizationId);
        return ResponseEntity.ok(exists);
    }

    /**
     * Récupère toutes les catégories de véhicule actives.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des catégories de véhicule actives
     */
    @GetMapping("/vehicle-categories")
    public ResponseEntity<List<VehicleCategoryDTO>> getVehicleCategories(@RequestParam UUID organizationId) {
        List<VehicleCategoryDTO> categories = referenceDataService.getActiveVehicleCategories(organizationId);
        return ResponseEntity.ok(categories);
    }

    /**
     * Récupère une catégorie de véhicule par son ID.
     *
     * @param id             L'ID de la catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La catégorie de véhicule trouvée, ou 404 si non trouvée
     */
    @GetMapping("/vehicle-categories/{id}")
    public ResponseEntity<VehicleCategoryDTO> getVehicleCategoryById(@PathVariable UUID id,
                                                                     @RequestParam UUID organizationId) {
        return referenceDataService.getVehicleCategoryById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère tous les types de carburant actifs.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carburant actifs
     */
    @GetMapping("/fuel-types")
    public ResponseEntity<List<FuelTypeDTO>> getFuelTypes(@RequestParam UUID organizationId) {
        List<FuelTypeDTO> fuelTypes = referenceDataService.getActiveFuelTypes(organizationId);
        return ResponseEntity.ok(fuelTypes);
    }

    /**
     * Récupère un type de carburant par son ID.
     *
     * @param id             L'ID du type de carburant
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant trouvé, ou 404 si non trouvé
     */
    @GetMapping("/fuel-types/{id}")
    public ResponseEntity<FuelTypeDTO> getFuelTypeById(@PathVariable UUID id,
                                                       @RequestParam UUID organizationId) {
        return referenceDataService.getFuelTypeById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère toutes les sous-catégories de véhicule actives.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des sous-catégories de véhicule actives
     */
    @GetMapping("/vehicle-subcategories")
    public ResponseEntity<List<VehicleSubcategoryDTO>> getVehicleSubcategories(@RequestParam UUID organizationId) {
        List<VehicleSubcategoryDTO> subcategories = referenceDataService.getActiveVehicleSubcategories(organizationId);
        return ResponseEntity.ok(subcategories);
    }

    /**
     * Récupère toutes les sous-catégories de véhicule actives pour une catégorie donnée.
     *
     * @param categoryId     L'ID de la catégorie
     * @param organizationId L'ID de l'organisation
     * @return La liste des sous-catégories de véhicule actives pour la catégorie
     */
    @GetMapping("/vehicle-categories/{categoryId}/subcategories")
    public ResponseEntity<List<VehicleSubcategoryDTO>> getVehicleSubcategoriesByCategory(
            @PathVariable UUID categoryId,
            @RequestParam UUID organizationId) {
        List<VehicleSubcategoryDTO> subcategories = referenceDataService.getActiveVehicleSubcategoriesByCategory(categoryId, organizationId);
        return ResponseEntity.ok(subcategories);
    }

    /**
     * Récupère une sous-catégorie de véhicule par son ID.
     *
     * @param id             L'ID de la sous-catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La sous-catégorie de véhicule trouvée, ou 404 si non trouvée
     */
    @GetMapping("/vehicle-subcategories/{id}")
    public ResponseEntity<VehicleSubcategoryDTO> getVehicleSubcategoryById(
            @PathVariable UUID id,
            @RequestParam UUID organizationId) {
        return referenceDataService.getVehicleSubcategoryById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère toutes les marques de véhicule actives.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des marques de véhicule actives
     */
    @GetMapping("/vehicle-makes")
    public ResponseEntity<List<VehicleMakeDTO>> getVehicleMakes(@RequestParam UUID organizationId) {
        List<VehicleMakeDTO> makes = referenceDataService.getActiveVehicleMakes(organizationId);
        return ResponseEntity.ok(makes);
    }

    /**
     * Récupère une marque de véhicule par son ID.
     *
     * @param id             L'ID de la marque de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La marque de véhicule trouvée, ou 404 si non trouvée
     */
    @GetMapping("/vehicle-makes/{id}")
    public ResponseEntity<VehicleMakeDTO> getVehicleMakeById(
            @PathVariable UUID id,
            @RequestParam UUID organizationId) {
        return referenceDataService.getVehicleMakeById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère tous les modèles de véhicule actifs.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des modèles de véhicule actifs
     */
    @GetMapping("/vehicle-models")
    public ResponseEntity<List<VehicleModelDTO>> getVehicleModels(@RequestParam UUID organizationId) {
        List<VehicleModelDTO> models = referenceDataService.getActiveVehicleModels(organizationId);
        return ResponseEntity.ok(models);
    }

    /**
     * Récupère tous les modèles de véhicule actifs pour une marque donnée.
     *
     * @param makeId         L'ID de la marque
     * @param organizationId L'ID de l'organisation
     * @return La liste des modèles de véhicule actifs pour la marque
     */
    @GetMapping("/vehicle-makes/{makeId}/models")
    public ResponseEntity<List<VehicleModelDTO>> getVehicleModelsByMake(
            @PathVariable UUID makeId,
            @RequestParam UUID organizationId) {
        List<VehicleModelDTO> models = referenceDataService.getActiveVehicleModelsByMake(makeId, organizationId);
        return ResponseEntity.ok(models);
    }

    /**
     * Récupère un modèle de véhicule par son ID.
     *
     * @param id             L'ID du modèle de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le modèle de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/vehicle-models/{id}")
    public ResponseEntity<VehicleModelDTO> getVehicleModelById(
            @PathVariable UUID id,
            @RequestParam UUID organizationId) {
        return referenceDataService.getVehicleModelById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère tous les types de carrosserie de véhicule actifs.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carrosserie de véhicule actifs
     */
    @GetMapping("/vehicle-body-types")
    public ResponseEntity<List<VehicleBodyTypeDTO>> getVehicleBodyTypes(@RequestParam UUID organizationId) {
        List<VehicleBodyTypeDTO> bodyTypes = referenceDataService.getActiveVehicleBodyTypes(organizationId);
        return ResponseEntity.ok(bodyTypes);
    }

    /**
     * Récupère un type de carrosserie de véhicule par son ID.
     *
     * @param id             L'ID du type de carrosserie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type de carrosserie de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/vehicle-body-types/{id}")
    public ResponseEntity<VehicleBodyTypeDTO> getVehicleBodyTypeById(
            @PathVariable UUID id,
            @RequestParam UUID organizationId) {
        return referenceDataService.getVehicleBodyTypeById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère tous les genres de véhicule actifs.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des genres de véhicule actifs
     */
    @GetMapping("/vehicle-genres")
    public ResponseEntity<List<VehicleGenreDTO>> getVehicleGenres(@RequestParam UUID organizationId) {
        List<VehicleGenreDTO> genres = referenceDataService.getActiveVehicleGenres(organizationId);
        return ResponseEntity.ok(genres);
    }

    /**
     * Récupère un genre de véhicule par son ID.
     *
     * @param id             L'ID du genre de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le genre de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/vehicle-genres/{id}")
    public ResponseEntity<VehicleGenreDTO> getVehicleGenreById(
            @PathVariable UUID id,
            @RequestParam UUID organizationId) {
        return referenceDataService.getVehicleGenreById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère toutes les zones de circulation actives.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des zones de circulation actives
     */
    @GetMapping("/circulation-zones")
    public ResponseEntity<List<CirculationZoneDTO>> getCirculationZones(@RequestParam UUID organizationId) {
        List<CirculationZoneDTO> zones = referenceDataService.getActiveCirculationZones(organizationId);
        return ResponseEntity.ok(zones);
    }

    /**
     * Récupère une zone de circulation par son ID.
     *
     * @param id             L'ID de la zone de circulation
     * @param organizationId L'ID de l'organisation
     * @return La zone de circulation trouvée, ou 404 si non trouvée
     */
    @GetMapping("/circulation-zones/{id}")
    public ResponseEntity<CirculationZoneDTO> getCirculationZoneById(
            @PathVariable UUID id,
            @RequestParam UUID organizationId) {
        return referenceDataService.getCirculationZoneById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère toutes les zones géographiques actives.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des zones géographiques actives
     */
    @GetMapping("/geographic-zones")
    public ResponseEntity<List<GeographicZoneDTO>> getGeographicZones(@RequestParam UUID organizationId) {
        List<GeographicZoneDTO> zones = referenceDataService.getActiveGeographicZones(organizationId);
        return ResponseEntity.ok(zones);
    }

    /**
     * Récupère une zone géographique par son ID.
     *
     * @param id             L'ID de la zone géographique
     * @param organizationId L'ID de l'organisation
     * @return La zone géographique trouvée, ou 404 si non trouvée
     */
    @GetMapping("/geographic-zones/{id}")
    public ResponseEntity<GeographicZoneDTO> getGeographicZoneById(
            @PathVariable UUID id,
            @RequestParam UUID organizationId) {
        return referenceDataService.getGeographicZoneById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère tous les types d'usage de véhicule actifs.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types d'usage de véhicule actifs
     */
    @GetMapping("/vehicle-usages")
    public ResponseEntity<List<VehicleUsageDTO>> getVehicleUsages(@RequestParam UUID organizationId) {
        List<VehicleUsageDTO> usages = referenceDataService.getActiveVehicleUsages(organizationId);
        return ResponseEntity.ok(usages);
    }

    /**
     * Récupère un type d'usage de véhicule par son ID.
     *
     * @param id             L'ID du type d'usage de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type d'usage de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/vehicle-usages/{id}")
    public ResponseEntity<VehicleUsageDTO> getVehicleUsageById(
            @PathVariable UUID id,
            @RequestParam UUID organizationId) {
        return referenceDataService.getVehicleUsageById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère toutes les couleurs de véhicule actives.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des couleurs de véhicule actives
     */
    @GetMapping("/vehicle-colors")
    public ResponseEntity<List<VehicleColorDTO>> getVehicleColors(@RequestParam UUID organizationId) {
        List<VehicleColorDTO> colors = referenceDataService.getActiveVehicleColors(organizationId);
        return ResponseEntity.ok(colors);
    }

    /**
     * Récupère une couleur de véhicule par son ID.
     *
     * @param id             L'ID de la couleur de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La couleur de véhicule trouvée, ou 404 si non trouvée
     */
    @GetMapping("/vehicle-colors/{id}")
    public ResponseEntity<VehicleColorDTO> getVehicleColorById(
            @PathVariable UUID id,
            @RequestParam UUID organizationId) {
        return referenceDataService.getVehicleColorById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
