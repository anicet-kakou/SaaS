package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleManufacturerDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleManufacturerService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleManufacturer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des fabricants de véhicule.
 *
 * @deprecated Utiliser {@link VehicleManufacturerReferenceController} à la place.
 * Ce contrôleur sera supprimé dans une version future.
 */
@Deprecated(since = "1.0.0", forRemoval = true)
@RestController
@RequestMapping("/api/v1/auto/reference/vehicle-manufacturers-management")
@RequiredArgsConstructor
public class VehicleManufacturerController {

    private final VehicleManufacturerService vehicleManufacturerService;

    /**
     * Récupère tous les fabricants de véhicule actifs.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des fabricants de véhicule actifs
     */
    @GetMapping
    public ResponseEntity<List<VehicleManufacturerDTO>> getAllActiveVehicleManufacturers(@RequestParam UUID organizationId) {
        List<VehicleManufacturerDTO> manufacturers = vehicleManufacturerService.getAllActiveVehicleManufacturers(organizationId);
        return ResponseEntity.ok(manufacturers);
    }

    /**
     * Récupère tous les fabricants de véhicule (actifs et inactifs).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des fabricants de véhicule
     */
    @GetMapping("/all")
    public ResponseEntity<List<VehicleManufacturerDTO>> getAllVehicleManufacturers(@RequestParam UUID organizationId) {
        List<VehicleManufacturerDTO> manufacturers = vehicleManufacturerService.getAllVehicleManufacturers(organizationId);
        return ResponseEntity.ok(manufacturers);
    }

    /**
     * Récupère un fabricant de véhicule par son ID.
     *
     * @param id             L'ID du fabricant de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le fabricant de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleManufacturerDTO> getVehicleManufacturerById(@PathVariable UUID id, @RequestParam UUID organizationId) {
        return vehicleManufacturerService.getVehicleManufacturerById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère un fabricant de véhicule par son code.
     *
     * @param code           Le code du fabricant de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le fabricant de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<VehicleManufacturerDTO> getVehicleManufacturerByCode(@PathVariable String code, @RequestParam UUID organizationId) {
        return vehicleManufacturerService.getVehicleManufacturerByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée un nouveau fabricant de véhicule.
     *
     * @param vehicleManufacturer Le fabricant de véhicule à créer
     * @param organizationId      L'ID de l'organisation
     * @return Le fabricant de véhicule créé
     */
    @PostMapping
    public ResponseEntity<VehicleManufacturerDTO> createVehicleManufacturer(@RequestBody VehicleManufacturer vehicleManufacturer, @RequestParam UUID organizationId) {
        VehicleManufacturerDTO createdManufacturer = vehicleManufacturerService.createVehicleManufacturer(vehicleManufacturer, organizationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdManufacturer);
    }

    /**
     * Met à jour un fabricant de véhicule.
     *
     * @param id                  L'ID du fabricant de véhicule
     * @param vehicleManufacturer Le fabricant de véhicule mis à jour
     * @param organizationId      L'ID de l'organisation
     * @return Le fabricant de véhicule mis à jour, ou 404 si non trouvé
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleManufacturerDTO> updateVehicleManufacturer(@PathVariable UUID id, @RequestBody VehicleManufacturer vehicleManufacturer, @RequestParam UUID organizationId) {
        return vehicleManufacturerService.updateVehicleManufacturer(id, vehicleManufacturer, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime un fabricant de véhicule.
     *
     * @param id             L'ID du fabricant de véhicule
     * @param organizationId L'ID de l'organisation
     * @return 204 si la suppression a réussi, 404 si non trouvé
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleManufacturer(@PathVariable UUID id, @RequestParam UUID organizationId) {
        boolean deleted = vehicleManufacturerService.deleteVehicleManufacturer(id, organizationId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
