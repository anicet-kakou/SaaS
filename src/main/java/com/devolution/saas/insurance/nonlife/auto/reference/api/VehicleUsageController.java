package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleUsageDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleUsageService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleUsage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des types d'usage de véhicule.
 *
 * @deprecated Utiliser {@link VehicleUsageReferenceController} à la place.
 * Ce contrôleur sera supprimé dans une version future.
 */
@Deprecated(since = "1.0.0", forRemoval = true)
@RestController
@RequestMapping("/api/v1/auto/reference/vehicle-usages-management")
@RequiredArgsConstructor
public class VehicleUsageController {

    private final VehicleUsageService vehicleUsageService;

    /**
     * Récupère tous les types d'usage de véhicule actifs.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types d'usage de véhicule actifs
     */
    @GetMapping
    public ResponseEntity<List<VehicleUsageDTO>> getAllActiveVehicleUsages(@RequestParam UUID organizationId) {
        List<VehicleUsageDTO> usages = vehicleUsageService.getAllActiveVehicleUsages(organizationId);
        return ResponseEntity.ok(usages);
    }

    /**
     * Récupère tous les types d'usage de véhicule (actifs et inactifs).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types d'usage de véhicule
     */
    @GetMapping("/all")
    public ResponseEntity<List<VehicleUsageDTO>> getAllVehicleUsages(@RequestParam UUID organizationId) {
        List<VehicleUsageDTO> usages = vehicleUsageService.getAllVehicleUsages(organizationId);
        return ResponseEntity.ok(usages);
    }

    /**
     * Récupère un type d'usage de véhicule par son ID.
     *
     * @param id             L'ID du type d'usage de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type d'usage de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleUsageDTO> getVehicleUsageById(@PathVariable UUID id, @RequestParam UUID organizationId) {
        return vehicleUsageService.getVehicleUsageById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère un type d'usage de véhicule par son code.
     *
     * @param code           Le code du type d'usage de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type d'usage de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<VehicleUsageDTO> getVehicleUsageByCode(@PathVariable String code, @RequestParam UUID organizationId) {
        return vehicleUsageService.getVehicleUsageByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée un nouveau type d'usage de véhicule.
     *
     * @param vehicleUsage   Le type d'usage de véhicule à créer
     * @param organizationId L'ID de l'organisation
     * @return Le type d'usage de véhicule créé
     */
    @PostMapping
    public ResponseEntity<VehicleUsageDTO> createVehicleUsage(@RequestBody VehicleUsage vehicleUsage, @RequestParam UUID organizationId) {
        VehicleUsageDTO createdUsage = vehicleUsageService.createVehicleUsage(vehicleUsage, organizationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUsage);
    }

    /**
     * Met à jour un type d'usage de véhicule.
     *
     * @param id             L'ID du type d'usage de véhicule
     * @param vehicleUsage   Le type d'usage de véhicule mis à jour
     * @param organizationId L'ID de l'organisation
     * @return Le type d'usage de véhicule mis à jour, ou 404 si non trouvé
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleUsageDTO> updateVehicleUsage(@PathVariable UUID id, @RequestBody VehicleUsage vehicleUsage, @RequestParam UUID organizationId) {
        return vehicleUsageService.updateVehicleUsage(id, vehicleUsage, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime un type d'usage de véhicule.
     *
     * @param id             L'ID du type d'usage de véhicule
     * @param organizationId L'ID de l'organisation
     * @return 204 si la suppression a réussi, 404 si non trouvé
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleUsage(@PathVariable UUID id, @RequestParam UUID organizationId) {
        boolean deleted = vehicleUsageService.deleteVehicleUsage(id, organizationId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
