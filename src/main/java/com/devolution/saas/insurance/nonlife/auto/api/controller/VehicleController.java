package com.devolution.saas.insurance.nonlife.auto.api.controller;

import com.devolution.saas.insurance.nonlife.auto.api.request.CreateVehicleRequest;
import com.devolution.saas.insurance.nonlife.auto.application.command.CreateVehicleCommand;
import com.devolution.saas.insurance.nonlife.auto.application.dto.VehicleDTO;
import com.devolution.saas.insurance.nonlife.auto.application.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des véhicules.
 *
 * @deprecated Utiliser {@link AutoVehicleController} à la place.
 * Ce contrôleur sera supprimé dans une version future.
 */
@Deprecated(since = "1.0.0", forRemoval = true)
@RestController
@RequestMapping("/api/v1/organizations/{organizationId}/vehicles")
@RequiredArgsConstructor
@Slf4j
public class VehicleController {

    private final VehicleService vehicleService;

    /**
     * Crée un nouveau véhicule.
     *
     * @param organizationId L'ID de l'organisation
     * @param request        La requête de création
     * @return Le véhicule créé
     */
    @PostMapping
    public ResponseEntity<VehicleDTO> createVehicle(
            @PathVariable UUID organizationId,
            @Valid @RequestBody CreateVehicleRequest request) {
        log.info("Création d'un nouveau véhicule pour l'organisation: {}", organizationId);

        CreateVehicleCommand command = mapRequestToCommand(request, organizationId);
        VehicleDTO vehicle = vehicleService.createVehicle(command);

        return new ResponseEntity<>(vehicle, HttpStatus.CREATED);
    }

    /**
     * Récupère un véhicule par son ID.
     *
     * @param organizationId L'ID de l'organisation
     * @param id             L'ID du véhicule
     * @return Le véhicule trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(
            @PathVariable UUID organizationId,
            @PathVariable UUID id) {
        log.info("Récupération du véhicule avec ID: {} pour l'organisation: {}", id, organizationId);

        VehicleDTO vehicle = vehicleService.getVehicleById(id, organizationId);
        return ResponseEntity.ok(vehicle);
    }

    /**
     * Récupère un véhicule par son numéro d'immatriculation.
     *
     * @param organizationId     L'ID de l'organisation
     * @param registrationNumber Le numéro d'immatriculation
     * @return Le véhicule trouvé
     */
    @GetMapping("/registration/{registrationNumber}")
    public ResponseEntity<VehicleDTO> getVehicleByRegistrationNumber(
            @PathVariable UUID organizationId,
            @PathVariable String registrationNumber) {
        log.info("Récupération du véhicule avec immatriculation: {} pour l'organisation: {}", registrationNumber, organizationId);

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
    public ResponseEntity<List<VehicleDTO>> getAllVehicles(
            @PathVariable UUID organizationId) {
        log.info("Listage de tous les véhicules pour l'organisation: {}", organizationId);

        List<VehicleDTO> vehicles = vehicleService.getAllVehicles(organizationId);
        return ResponseEntity.ok(vehicles);
    }

    /**
     * Liste tous les véhicules d'un propriétaire.
     *
     * @param organizationId L'ID de l'organisation
     * @param ownerId        L'ID du propriétaire
     * @return La liste des véhicules
     */
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByOwner(
            @PathVariable UUID organizationId,
            @PathVariable UUID ownerId) {
        log.info("Listage des véhicules pour le propriétaire: {} dans l'organisation: {}", ownerId, organizationId);

        List<VehicleDTO> vehicles = vehicleService.getVehiclesByOwner(ownerId, organizationId);
        return ResponseEntity.ok(vehicles);
    }

    /**
     * Met à jour un véhicule.
     *
     * @param organizationId L'ID de l'organisation
     * @param id             L'ID du véhicule
     * @param request        La requête de mise à jour
     * @return Le véhicule mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(
            @PathVariable UUID organizationId,
            @PathVariable UUID id,
            @Valid @RequestBody CreateVehicleRequest request) {
        log.info("Mise à jour du véhicule avec ID: {} pour l'organisation: {}", id, organizationId);

        CreateVehicleCommand command = mapRequestToCommand(request, organizationId);
        VehicleDTO vehicle = vehicleService.updateVehicle(id, command);

        return ResponseEntity.ok(vehicle);
    }

    /**
     * Supprime un véhicule.
     *
     * @param organizationId L'ID de l'organisation
     * @param id             L'ID du véhicule
     * @return Réponse vide avec statut 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable UUID organizationId,
            @PathVariable UUID id) {
        log.info("Suppression du véhicule avec ID: {} pour l'organisation: {}", id, organizationId);

        vehicleService.deleteVehicle(id, organizationId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Convertit une requête en commande.
     *
     * @param request        La requête à convertir
     * @param organizationId L'ID de l'organisation
     * @return La commande correspondante
     */
    private CreateVehicleCommand mapRequestToCommand(CreateVehicleRequest request, UUID organizationId) {
        return CreateVehicleCommand.builder()
                .registrationNumber(request.getRegistrationNumber())
                .makeId(request.getMakeId())
                .modelId(request.getModelId())
                .modelVariant(request.getVersion())
                .year(request.getYear())
                .enginePower(request.getEnginePower())
                .engineSize(request.getEngineSize())
                .fuelTypeId(request.getFuelTypeId())
                .categoryId(request.getCategoryId())
                .subcategoryId(request.getSubcategoryId())
                .usageId(request.getUsageId())
                .geographicZoneId(request.getGeographicZoneId())
                .purchaseDate(request.getPurchaseDate())
                .purchaseValue(request.getPurchaseValue())
                .currentValue(request.getCurrentValue())
                .mileage(request.getMileage())
                .vin(request.getVin())
                .colorId(request.getColorId())
                .ownerId(request.getOwnerId())
                .organizationId(organizationId)
                .build();
    }
}
