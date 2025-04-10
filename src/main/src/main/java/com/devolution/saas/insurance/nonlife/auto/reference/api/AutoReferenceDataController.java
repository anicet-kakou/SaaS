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
public class AutoReferenceDataController {

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

    // Les autres endpoints seront ajoutés au fur et à mesure de l'implémentation des services correspondants
}
