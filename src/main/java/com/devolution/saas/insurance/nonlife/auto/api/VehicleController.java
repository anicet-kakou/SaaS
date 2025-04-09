package com.devolution.saas.insurance.nonlife.auto.api;

import com.devolution.saas.insurance.nonlife.auto.application.command.CreateVehicleCommand;
import com.devolution.saas.insurance.nonlife.auto.application.dto.VehicleDTO;
import com.devolution.saas.insurance.nonlife.auto.application.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des véhicules.
 */
@RestController
@RequestMapping("/api/v1/auto/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    /**
     * Crée un nouveau véhicule.
     *
     * @param command La commande de création
     * @return Le véhicule créé
     */
    @PostMapping
    public ResponseEntity<VehicleDTO> createVehicle(@RequestBody CreateVehicleCommand command) {
        VehicleDTO vehicle = vehicleService.createVehicle(command);
        return new ResponseEntity<>(vehicle, HttpStatus.CREATED);
    }

    /**
     * Récupère un véhicule par son ID.
     *
     * @param id             L'ID du véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le véhicule trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable UUID id,
                                                     @RequestParam UUID organizationId) {
        VehicleDTO vehicle = vehicleService.getVehicleById(id, organizationId);
        return ResponseEntity.ok(vehicle);
    }

    /**
     * Récupère un véhicule par son numéro d'immatriculation.
     *
     * @param registrationNumber Le numéro d'immatriculation
     * @param organizationId     L'ID de l'organisation
     * @return Le véhicule trouvé
     */
    @GetMapping("/registration/{registrationNumber}")
    public ResponseEntity<VehicleDTO> getVehicleByRegistrationNumber(@PathVariable String registrationNumber,
                                                                     @RequestParam UUID organizationId) {
        VehicleDTO vehicle = vehicleService.getVehicleByRegistrationNumber(registrationNumber, organizationId);
        return ResponseEntity.ok(vehicle);
    }

    /**
     * Liste tous les véhicules d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des véhicules
     */
    @GetMapping
    public ResponseEntity<List<VehicleDTO>> getAllVehicles(@RequestParam UUID organizationId) {
        List<VehicleDTO> vehicles = vehicleService.getAllVehicles(organizationId);
        return ResponseEntity.ok(vehicles);
    }

    /**
     * Liste tous les véhicules d'un propriétaire.
     *
     * @param ownerId        L'ID du propriétaire
     * @param organizationId L'ID de l'organisation
     * @return La liste des véhicules
     */
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByOwner(@PathVariable UUID ownerId,
                                                               @RequestParam UUID organizationId) {
        List<VehicleDTO> vehicles = vehicleService.getVehiclesByOwner(ownerId, organizationId);
        return ResponseEntity.ok(vehicles);
    }

    /**
     * Met à jour un véhicule.
     *
     * @param id      L'ID du véhicule
     * @param command La commande de mise à jour
     * @return Le véhicule mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable UUID id,
                                                    @RequestBody CreateVehicleCommand command) {
        VehicleDTO vehicle = vehicleService.updateVehicle(id, command);
        return ResponseEntity.ok(vehicle);
    }

    /**
     * Supprime un véhicule.
     *
     * @param id             L'ID du véhicule
     * @param organizationId L'ID de l'organisation
     * @return Réponse vide avec statut 204
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable UUID id,
                                              @RequestParam UUID organizationId) {
        vehicleService.deleteVehicle(id, organizationId);
        return ResponseEntity.noContent().build();
    }
}
