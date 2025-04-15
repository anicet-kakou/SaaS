package com.devolution.saas.insurance.nonlife.auto.api.controller;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.insurance.nonlife.auto.api.dto.request.CreateVehicleRequest;
import com.devolution.saas.insurance.nonlife.auto.api.dto.response.VehicleResponse;
import com.devolution.saas.insurance.nonlife.auto.api.mapper.VehicleApiMapper;
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
    private final VehicleApiMapper vehicleApiMapper;

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
    public ResponseEntity<VehicleResponse> createVehicle(
            @Valid @RequestBody CreateVehicleRequest request,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour créer un nouveau véhicule pour l'organisation: {}", organizationId);

        CreateVehicleCommand command = vehicleApiMapper.toEntity(request, organizationId);
        VehicleDTO vehicleDTO = vehicleService.createVehicle(command);
        VehicleResponse response = vehicleApiMapper.toResponse(vehicleDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
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
    public ResponseEntity<VehicleResponse> getVehicleById(
            @PathVariable UUID id,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer le véhicule avec ID: {} pour l'organisation: {}", id, organizationId);

        VehicleDTO vehicleDTO = vehicleService.getVehicleById(id, organizationId);
        VehicleResponse response = vehicleApiMapper.toResponse(vehicleDTO);
        return ResponseEntity.ok(response);
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
    public ResponseEntity<VehicleResponse> getVehicleByRegistrationNumber(
            @PathVariable String registrationNumber,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer le véhicule avec immatriculation: {} pour l'organisation: {}",
                registrationNumber, organizationId);

        VehicleDTO vehicleDTO = vehicleService.getVehicleByRegistrationNumber(registrationNumber, organizationId);
        VehicleResponse response = vehicleApiMapper.toResponse(vehicleDTO);
        return ResponseEntity.ok(response);
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
    public ResponseEntity<List<VehicleResponse>> getAllVehicles(
            @RequestParam UUID organizationId) {
        log.debug("REST request pour lister tous les véhicules pour l'organisation: {}", organizationId);

        List<VehicleDTO> vehicleDTOs = vehicleService.getAllVehicles(organizationId);
        List<VehicleResponse> responses = vehicleDTOs.stream()
                .map(vehicleApiMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
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
    public ResponseEntity<List<VehicleResponse>> getVehiclesByOwner(
            @PathVariable UUID ownerId,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour lister les véhicules pour le propriétaire: {} dans l'organisation: {}",
                ownerId, organizationId);

        List<VehicleDTO> vehicleDTOs = vehicleService.getVehiclesByOwner(ownerId, organizationId);
        List<VehicleResponse> responses = vehicleDTOs.stream()
                .map(vehicleApiMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
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
    public ResponseEntity<VehicleResponse> updateVehicle(
            @PathVariable UUID id,
            @Valid @RequestBody CreateVehicleRequest request,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour mettre à jour le véhicule avec ID: {} pour l'organisation: {}",
                id, organizationId);

        CreateVehicleCommand command = vehicleApiMapper.toEntity(request, organizationId);
        VehicleDTO vehicleDTO = vehicleService.updateVehicle(id, command);
        VehicleResponse response = vehicleApiMapper.toResponse(vehicleDTO);

        return ResponseEntity.ok(response);
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


}
