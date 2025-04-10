package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleBodyTypeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleBodyTypeService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleBodyType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
public class VehicleBodyTypeController {

    private final VehicleBodyTypeService vehicleBodyTypeService;

    /**
     * Récupère tous les types de carrosserie de véhicule actifs.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carrosserie de véhicule actifs
     */
    @GetMapping
    public ResponseEntity<List<VehicleBodyTypeDTO>> getAllActiveVehicleBodyTypes(@RequestParam UUID organizationId) {
        List<VehicleBodyTypeDTO> bodyTypes = vehicleBodyTypeService.getAllActiveVehicleBodyTypes(organizationId);
        return ResponseEntity.ok(bodyTypes);
    }

    /**
     * Récupère tous les types de carrosserie de véhicule (actifs et inactifs).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carrosserie de véhicule
     */
    @GetMapping("/all")
    public ResponseEntity<List<VehicleBodyTypeDTO>> getAllVehicleBodyTypes(@RequestParam UUID organizationId) {
        List<VehicleBodyTypeDTO> bodyTypes = vehicleBodyTypeService.getAllVehicleBodyTypes(organizationId);
        return ResponseEntity.ok(bodyTypes);
    }

    /**
     * Récupère un type de carrosserie de véhicule par son ID.
     *
     * @param id             L'ID du type de carrosserie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type de carrosserie de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleBodyTypeDTO> getVehicleBodyTypeById(@PathVariable UUID id, @RequestParam UUID organizationId) {
        return vehicleBodyTypeService.getVehicleBodyTypeById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère un type de carrosserie de véhicule par son code.
     *
     * @param code           Le code du type de carrosserie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type de carrosserie de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<VehicleBodyTypeDTO> getVehicleBodyTypeByCode(@PathVariable String code, @RequestParam UUID organizationId) {
        return vehicleBodyTypeService.getVehicleBodyTypeByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée un nouveau type de carrosserie de véhicule.
     *
     * @param vehicleBodyType Le type de carrosserie de véhicule à créer
     * @param organizationId  L'ID de l'organisation
     * @return Le type de carrosserie de véhicule créé
     */
    @PostMapping
    public ResponseEntity<VehicleBodyTypeDTO> createVehicleBodyType(@RequestBody VehicleBodyType vehicleBodyType, @RequestParam UUID organizationId) {
        VehicleBodyTypeDTO createdBodyType = vehicleBodyTypeService.createVehicleBodyType(vehicleBodyType, organizationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBodyType);
    }

    /**
     * Met à jour un type de carrosserie de véhicule.
     *
     * @param id              L'ID du type de carrosserie de véhicule
     * @param vehicleBodyType Le type de carrosserie de véhicule mis à jour
     * @param organizationId  L'ID de l'organisation
     * @return Le type de carrosserie de véhicule mis à jour, ou 404 si non trouvé
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleBodyTypeDTO> updateVehicleBodyType(@PathVariable UUID id, @RequestBody VehicleBodyType vehicleBodyType, @RequestParam UUID organizationId) {
        return vehicleBodyTypeService.updateVehicleBodyType(id, vehicleBodyType, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime un type de carrosserie de véhicule.
     *
     * @param id             L'ID du type de carrosserie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return 204 si la suppression a réussi, 404 si non trouvé
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleBodyType(@PathVariable UUID id, @RequestParam UUID organizationId) {
        boolean deleted = vehicleBodyTypeService.deleteVehicleBodyType(id, organizationId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
