package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleFuelTypeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleFuelTypeService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleFuelType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des types de carburant de véhicule.
 *
 * @deprecated Utiliser {@link VehicleFuelTypeReferenceController} à la place.
 * Ce contrôleur sera supprimé dans une version future.
 */
@Deprecated(since = "1.0.0", forRemoval = true)
@RestController
@RequestMapping("/api/v1/auto/reference/vehicle-fuel-types-management")
@RequiredArgsConstructor
public class VehicleFuelTypeController {

    private final VehicleFuelTypeService vehicleFuelTypeService;

    /**
     * Récupère tous les types de carburant de véhicule actifs.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carburant de véhicule actifs
     */
    @GetMapping
    public ResponseEntity<List<VehicleFuelTypeDTO>> getAllActiveVehicleFuelTypes(@RequestParam UUID organizationId) {
        List<VehicleFuelTypeDTO> fuelTypes = vehicleFuelTypeService.getAllActiveVehicleFuelTypes(organizationId);
        return ResponseEntity.ok(fuelTypes);
    }

    /**
     * Récupère tous les types de carburant de véhicule (actifs et inactifs).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carburant de véhicule
     */
    @GetMapping("/all")
    public ResponseEntity<List<VehicleFuelTypeDTO>> getAllVehicleFuelTypes(@RequestParam UUID organizationId) {
        List<VehicleFuelTypeDTO> fuelTypes = vehicleFuelTypeService.getAllVehicleFuelTypes(organizationId);
        return ResponseEntity.ok(fuelTypes);
    }

    /**
     * Récupère un type de carburant de véhicule par son ID.
     *
     * @param id             L'ID du type de carburant de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleFuelTypeDTO> getVehicleFuelTypeById(@PathVariable UUID id, @RequestParam UUID organizationId) {
        return vehicleFuelTypeService.getVehicleFuelTypeById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère un type de carburant de véhicule par son code.
     *
     * @param code           Le code du type de carburant de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<VehicleFuelTypeDTO> getVehicleFuelTypeByCode(@PathVariable String code, @RequestParam UUID organizationId) {
        return vehicleFuelTypeService.getVehicleFuelTypeByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée un nouveau type de carburant de véhicule.
     *
     * @param vehicleFuelType Le type de carburant de véhicule à créer
     * @param organizationId  L'ID de l'organisation
     * @return Le type de carburant de véhicule créé
     */
    @PostMapping
    public ResponseEntity<VehicleFuelTypeDTO> createVehicleFuelType(@RequestBody VehicleFuelType vehicleFuelType, @RequestParam UUID organizationId) {
        VehicleFuelTypeDTO createdFuelType = vehicleFuelTypeService.createVehicleFuelType(vehicleFuelType, organizationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFuelType);
    }

    /**
     * Met à jour un type de carburant de véhicule.
     *
     * @param id              L'ID du type de carburant de véhicule
     * @param vehicleFuelType Le type de carburant de véhicule mis à jour
     * @param organizationId  L'ID de l'organisation
     * @return Le type de carburant de véhicule mis à jour, ou 404 si non trouvé
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleFuelTypeDTO> updateVehicleFuelType(@PathVariable UUID id, @RequestBody VehicleFuelType vehicleFuelType, @RequestParam UUID organizationId) {
        return vehicleFuelTypeService.updateVehicleFuelType(id, vehicleFuelType, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime un type de carburant de véhicule.
     *
     * @param id             L'ID du type de carburant de véhicule
     * @param organizationId L'ID de l'organisation
     * @return 204 si la suppression a réussi, 404 si non trouvé
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleFuelType(@PathVariable UUID id, @RequestParam UUID organizationId) {
        boolean deleted = vehicleFuelTypeService.deleteVehicleFuelType(id, organizationId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
