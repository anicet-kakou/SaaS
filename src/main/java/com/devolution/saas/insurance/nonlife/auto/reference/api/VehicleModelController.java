package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleModelDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleModelService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des modèles de véhicule.
 *
 * @deprecated Utiliser {@link VehicleModelReferenceController} à la place.
 * Ce contrôleur sera supprimé dans une version future.
 */
@Deprecated(since = "1.0.0", forRemoval = true)
@RestController
@RequestMapping("/api/v1/auto/reference/vehicle-models-management")
@RequiredArgsConstructor
public class VehicleModelController {

    private final VehicleModelService vehicleModelService;

    /**
     * Récupère tous les modèles de véhicule actifs.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des modèles de véhicule actifs
     */
    @GetMapping
    public ResponseEntity<List<VehicleModelDTO>> getAllActiveVehicleModels(@RequestParam UUID organizationId) {
        List<VehicleModelDTO> models = vehicleModelService.getAllActiveVehicleModels(organizationId);
        return ResponseEntity.ok(models);
    }

    /**
     * Récupère tous les modèles de véhicule (actifs et inactifs).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des modèles de véhicule
     */
    @GetMapping("/all")
    public ResponseEntity<List<VehicleModelDTO>> getAllVehicleModels(@RequestParam UUID organizationId) {
        List<VehicleModelDTO> models = vehicleModelService.getAllVehicleModels(organizationId);
        return ResponseEntity.ok(models);
    }

    /**
     * Récupère tous les modèles de véhicule actifs pour une marque donnée.
     *
     * @param makeId         L'ID de la marque
     * @param organizationId L'ID de l'organisation
     * @return La liste des modèles de véhicule actifs pour la marque
     */
    @GetMapping("/make/{makeId}")
    public ResponseEntity<List<VehicleModelDTO>> getAllActiveVehicleModelsByMake(@PathVariable UUID makeId, @RequestParam UUID organizationId) {
        List<VehicleModelDTO> models = vehicleModelService.getAllActiveVehicleModelsByMake(makeId, organizationId);
        return ResponseEntity.ok(models);
    }

    /**
     * Récupère tous les modèles de véhicule (actifs et inactifs) pour une marque donnée.
     *
     * @param makeId         L'ID de la marque
     * @param organizationId L'ID de l'organisation
     * @return La liste des modèles de véhicule pour la marque
     */
    @GetMapping("/make/{makeId}/all")
    public ResponseEntity<List<VehicleModelDTO>> getAllVehicleModelsByMake(@PathVariable UUID makeId, @RequestParam UUID organizationId) {
        List<VehicleModelDTO> models = vehicleModelService.getAllVehicleModelsByMake(makeId, organizationId);
        return ResponseEntity.ok(models);
    }

    /**
     * Récupère un modèle de véhicule par son ID.
     *
     * @param id             L'ID du modèle de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le modèle de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleModelDTO> getVehicleModelById(@PathVariable UUID id, @RequestParam UUID organizationId) {
        return vehicleModelService.getVehicleModelById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère un modèle de véhicule par son code et sa marque.
     *
     * @param code           Le code du modèle de véhicule
     * @param makeId         L'ID de la marque
     * @param organizationId L'ID de l'organisation
     * @return Le modèle de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/code/{code}/make/{makeId}")
    public ResponseEntity<VehicleModelDTO> getVehicleModelByCodeAndMake(@PathVariable String code, @PathVariable UUID makeId, @RequestParam UUID organizationId) {
        return vehicleModelService.getVehicleModelByCodeAndMake(code, makeId, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée un nouveau modèle de véhicule.
     *
     * @param vehicleModel   Le modèle de véhicule à créer
     * @param organizationId L'ID de l'organisation
     * @return Le modèle de véhicule créé
     */
    @PostMapping
    public ResponseEntity<VehicleModelDTO> createVehicleModel(@RequestBody VehicleModel vehicleModel, @RequestParam UUID organizationId) {
        VehicleModelDTO createdModel = vehicleModelService.createVehicleModel(vehicleModel, organizationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdModel);
    }

    /**
     * Met à jour un modèle de véhicule.
     *
     * @param id             L'ID du modèle de véhicule
     * @param vehicleModel   Le modèle de véhicule mis à jour
     * @param organizationId L'ID de l'organisation
     * @return Le modèle de véhicule mis à jour, ou 404 si non trouvé
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleModelDTO> updateVehicleModel(@PathVariable UUID id, @RequestBody VehicleModel vehicleModel, @RequestParam UUID organizationId) {
        return vehicleModelService.updateVehicleModel(id, vehicleModel, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime un modèle de véhicule.
     *
     * @param id             L'ID du modèle de véhicule
     * @param organizationId L'ID de l'organisation
     * @return 204 si la suppression a réussi, 404 si non trouvé
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleModel(@PathVariable UUID id, @RequestParam UUID organizationId) {
        boolean deleted = vehicleModelService.deleteVehicleModel(id, organizationId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
