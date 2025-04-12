package com.devolution.saas.insurance.nonlife.auto.api.controller;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.insurance.nonlife.auto.api.request.CreateVehicleRequest;
import com.devolution.saas.insurance.nonlife.auto.application.command.CreateVehicleCommand;
import com.devolution.saas.insurance.nonlife.auto.application.dto.VehicleDTO;
import com.devolution.saas.insurance.nonlife.auto.application.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des véhicules automobiles.
 */
@RestController
@RequestMapping("/api/v1/auto/vehicles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Auto Vehicles", description = "API pour la gestion des véhicules automobiles")
public class AutoVehicleController {

    private final VehicleService vehicleService;

    /**
     * Crée un nouveau véhicule.
     *
     * @param request        La requête de création
     * @param organizationId L'ID de l'organisation
     * @return Le véhicule créé
     */
    @PostMapping
    @Operation(summary = "Crée un nouveau véhicule")
    @Auditable(action = "API_CREATE_AUTO_VEHICLE")
    @TenantRequired
    public ResponseEntity<VehicleDTO> createVehicle(
            @Valid @RequestBody CreateVehicleRequest request,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour créer un nouveau véhicule pour l'organisation: {}", organizationId);

        CreateVehicleCommand command = mapRequestToCommand(request, organizationId);
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
    @Operation(summary = "Récupère un véhicule par son ID")
    @Auditable(action = "API_GET_AUTO_VEHICLE")
    @TenantRequired
    public ResponseEntity<VehicleDTO> getVehicleById(
            @PathVariable UUID id,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer le véhicule avec ID: {} pour l'organisation: {}", id, organizationId);

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
    @Operation(summary = "Récupère un véhicule par son numéro d'immatriculation")
    @Auditable(action = "API_GET_AUTO_VEHICLE_BY_REGISTRATION")
    @TenantRequired
    public ResponseEntity<VehicleDTO> getVehicleByRegistrationNumber(
            @PathVariable String registrationNumber,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer le véhicule avec immatriculation: {} pour l'organisation: {}",
                registrationNumber, organizationId);

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
    @Operation(summary = "Liste tous les véhicules d'une organisation")
    @Auditable(action = "API_LIST_AUTO_VEHICLES")
    @TenantRequired
    public ResponseEntity<List<VehicleDTO>> getAllVehicles(
            @RequestParam UUID organizationId) {
        log.debug("REST request pour lister tous les véhicules pour l'organisation: {}", organizationId);

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
    @Operation(summary = "Liste tous les véhicules d'un propriétaire")
    @Auditable(action = "API_LIST_AUTO_VEHICLES_BY_OWNER")
    @TenantRequired
    public ResponseEntity<List<VehicleDTO>> getVehiclesByOwner(
            @PathVariable UUID ownerId,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour lister les véhicules pour le propriétaire: {} dans l'organisation: {}",
                ownerId, organizationId);

        List<VehicleDTO> vehicles = vehicleService.getVehiclesByOwner(ownerId, organizationId);
        return ResponseEntity.ok(vehicles);
    }

    /**
     * Met à jour un véhicule.
     *
     * @param id             L'ID du véhicule
     * @param request        La requête de mise à jour
     * @param organizationId L'ID de l'organisation
     * @return Le véhicule mis à jour
     */
    @PutMapping("/{id}")
    @Operation(summary = "Met à jour un véhicule")
    @Auditable(action = "API_UPDATE_AUTO_VEHICLE")
    @TenantRequired
    public ResponseEntity<VehicleDTO> updateVehicle(
            @PathVariable UUID id,
            @Valid @RequestBody CreateVehicleRequest request,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour mettre à jour le véhicule avec ID: {} pour l'organisation: {}",
                id, organizationId);

        CreateVehicleCommand command = mapRequestToCommand(request, organizationId);
        VehicleDTO vehicle = vehicleService.updateVehicle(id, command);

        return ResponseEntity.ok(vehicle);
    }

    /**
     * Supprime un véhicule.
     *
     * @param id             L'ID du véhicule
     * @param organizationId L'ID de l'organisation
     * @return Réponse vide avec statut 204 No Content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime un véhicule")
    @Auditable(action = "API_DELETE_AUTO_VEHICLE")
    @TenantRequired
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable UUID id,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour supprimer le véhicule avec ID: {} pour l'organisation: {}",
                id, organizationId);

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
