package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleMakeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleMakeService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleMake;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des marques de véhicule.
 */
@RestController
@RequestMapping("/api/v1/auto/reference/vehicle-makes")
@RequiredArgsConstructor
public class VehicleMakeController {

    private final VehicleMakeService vehicleMakeService;

    /**
     * Récupère toutes les marques de véhicule actives.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des marques de véhicule actives
     */
    @GetMapping
    public ResponseEntity<List<VehicleMakeDTO>> getAllActiveVehicleMakes(@RequestParam UUID organizationId) {
        List<VehicleMakeDTO> makes = vehicleMakeService.getAllActiveVehicleMakes(organizationId);
        return ResponseEntity.ok(makes);
    }

    /**
     * Récupère toutes les marques de véhicule (actives et inactives).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des marques de véhicule
     */
    @GetMapping("/all")
    public ResponseEntity<List<VehicleMakeDTO>> getAllVehicleMakes(@RequestParam UUID organizationId) {
        List<VehicleMakeDTO> makes = vehicleMakeService.getAllVehicleMakes(organizationId);
        return ResponseEntity.ok(makes);
    }

    /**
     * Récupère une marque de véhicule par son ID.
     *
     * @param id             L'ID de la marque de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La marque de véhicule trouvée, ou 404 si non trouvée
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleMakeDTO> getVehicleMakeById(@PathVariable UUID id, @RequestParam UUID organizationId) {
        return vehicleMakeService.getVehicleMakeById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère une marque de véhicule par son code.
     *
     * @param code           Le code de la marque de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La marque de véhicule trouvée, ou 404 si non trouvée
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<VehicleMakeDTO> getVehicleMakeByCode(@PathVariable String code, @RequestParam UUID organizationId) {
        return vehicleMakeService.getVehicleMakeByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée une nouvelle marque de véhicule.
     *
     * @param vehicleMake    La marque de véhicule à créer
     * @param organizationId L'ID de l'organisation
     * @return La marque de véhicule créée
     */
    @PostMapping
    public ResponseEntity<VehicleMakeDTO> createVehicleMake(@RequestBody VehicleMake vehicleMake, @RequestParam UUID organizationId) {
        VehicleMakeDTO createdMake = vehicleMakeService.createVehicleMake(vehicleMake, organizationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMake);
    }

    /**
     * Met à jour une marque de véhicule.
     *
     * @param id             L'ID de la marque de véhicule
     * @param vehicleMake    La marque de véhicule mise à jour
     * @param organizationId L'ID de l'organisation
     * @return La marque de véhicule mise à jour, ou 404 si non trouvée
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleMakeDTO> updateVehicleMake(@PathVariable UUID id, @RequestBody VehicleMake vehicleMake, @RequestParam UUID organizationId) {
        return vehicleMakeService.updateVehicleMake(id, vehicleMake, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime une marque de véhicule.
     *
     * @param id             L'ID de la marque de véhicule
     * @param organizationId L'ID de l'organisation
     * @return 204 si la suppression a réussi, 404 si non trouvée
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleMake(@PathVariable UUID id, @RequestParam UUID organizationId) {
        boolean deleted = vehicleMakeService.deleteVehicleMake(id, organizationId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
