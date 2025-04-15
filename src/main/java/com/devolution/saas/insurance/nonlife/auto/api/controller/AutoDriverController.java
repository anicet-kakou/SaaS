package com.devolution.saas.insurance.nonlife.auto.api.controller;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.insurance.nonlife.auto.api.dto.request.CreateDriverRequest;
import com.devolution.saas.insurance.nonlife.auto.api.dto.response.DriverResponse;
import com.devolution.saas.insurance.nonlife.auto.api.mapper.DriverApiMapper;
import com.devolution.saas.insurance.nonlife.auto.application.dto.DriverDTO;
import com.devolution.saas.insurance.nonlife.auto.application.service.DriverService;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Driver;
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
 * Contrôleur REST pour la gestion des conducteurs automobiles.
 */
@RestController
@RequestMapping("/api/v1/auto/drivers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Auto Drivers", description = "API pour la gestion des conducteurs automobiles")
public class AutoDriverController {

    private final DriverService driverService;
    private final DriverApiMapper driverApiMapper;

    /**
     * Crée un nouveau conducteur.
     *
     * @param request        La requête de création
     * @param organizationId L'ID de l'organisation
     * @return Le conducteur créé
     */
    @PostMapping
    @Operation(summary = "Crée un nouveau conducteur")
    @Auditable(action = "API_CREATE_AUTO_DRIVER")
    @TenantRequired
    public ResponseEntity<DriverResponse> createDriver(
            @Valid @RequestBody CreateDriverRequest request,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour créer un nouveau conducteur pour l'organisation: {}", organizationId);

        Driver driver = driverApiMapper.toEntity(request);
        DriverDTO driverDTO = driverService.createDriver(driver, organizationId);
        DriverResponse response = driverApiMapper.toResponse(driverDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Récupère un conducteur par son ID.
     *
     * @param id             L'ID du conducteur
     * @param organizationId L'ID de l'organisation
     * @return Le conducteur trouvé
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupère un conducteur par son ID")
    @Auditable(action = "API_GET_AUTO_DRIVER")
    @TenantRequired
    public ResponseEntity<DriverResponse> getDriverById(
            @PathVariable UUID id,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer le conducteur avec ID: {} pour l'organisation: {}", id, organizationId);

        DriverDTO driverDTO = driverService.getDriverById(id, organizationId)
                .orElseThrow(() -> com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException.forId("Conducteur", id));
        DriverResponse response = driverApiMapper.toResponse(driverDTO);

        return ResponseEntity.ok(response);
    }

    /**
     * Récupère un conducteur par son numéro de permis.
     *
     * @param licenseNumber  Le numéro de permis
     * @param organizationId L'ID de l'organisation
     * @return Le conducteur trouvé
     */
    @GetMapping("/license/{licenseNumber}")
    @Operation(summary = "Récupère un conducteur par son numéro de permis")
    @Auditable(action = "API_GET_AUTO_DRIVER_BY_LICENSE")
    @TenantRequired
    public ResponseEntity<DriverResponse> getDriverByLicenseNumber(
            @PathVariable String licenseNumber,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer le conducteur avec numéro de permis: {} pour l'organisation: {}",
                licenseNumber, organizationId);

        DriverDTO driverDTO = driverService.getDriverByLicenseNumber(licenseNumber, organizationId)
                .orElseThrow(() -> com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException
                        .forIdentifier("Conducteur", "numéro de permis", licenseNumber));
        DriverResponse response = driverApiMapper.toResponse(driverDTO);

        return ResponseEntity.ok(response);
    }

    /**
     * Liste tous les conducteurs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des conducteurs
     */
    @GetMapping
    @Operation(summary = "Liste tous les conducteurs d'une organisation")
    @Auditable(action = "API_LIST_AUTO_DRIVERS")
    @TenantRequired
    public ResponseEntity<List<DriverResponse>> getAllDrivers(
            @RequestParam UUID organizationId) {
        log.debug("REST request pour lister tous les conducteurs pour l'organisation: {}", organizationId);

        List<DriverDTO> driverDTOs = driverService.getAllDrivers(organizationId);
        List<DriverResponse> responses = driverDTOs.stream()
                .map(driverApiMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Liste tous les conducteurs d'un client.
     *
     * @param customerId     L'ID du client
     * @param organizationId L'ID de l'organisation
     * @return La liste des conducteurs
     */
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Liste tous les conducteurs d'un client")
    @Auditable(action = "API_LIST_AUTO_DRIVERS_BY_CUSTOMER")
    @TenantRequired
    public ResponseEntity<List<DriverResponse>> getDriversByCustomer(
            @PathVariable UUID customerId,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour lister les conducteurs pour le client: {} dans l'organisation: {}",
                customerId, organizationId);

        List<DriverDTO> driverDTOs = driverService.getDriversByCustomer(customerId, organizationId);
        List<DriverResponse> responses = driverDTOs.stream()
                .map(driverApiMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Récupère le conducteur principal d'un client.
     *
     * @param customerId     L'ID du client
     * @param organizationId L'ID de l'organisation
     * @return Le conducteur principal
     */
    @GetMapping("/customer/{customerId}/primary")
    @Operation(summary = "Récupère le conducteur principal d'un client")
    @Auditable(action = "API_GET_PRIMARY_AUTO_DRIVER")
    @TenantRequired
    public ResponseEntity<DriverResponse> getPrimaryDriverByCustomer(
            @PathVariable UUID customerId,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer le conducteur principal pour le client: {} dans l'organisation: {}",
                customerId, organizationId);

        DriverDTO driverDTO = driverService.getPrimaryDriverByCustomer(customerId, organizationId)
                .orElseThrow(() -> com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException
                        .forIdentifier("Conducteur principal", "client", customerId.toString()));
        DriverResponse response = driverApiMapper.toResponse(driverDTO);

        return ResponseEntity.ok(response);
    }

    /**
     * Met à jour un conducteur.
     *
     * @param id             L'ID du conducteur
     * @param request        La requête de mise à jour
     * @param organizationId L'ID de l'organisation
     * @return Le conducteur mis à jour
     */
    @PutMapping("/{id}")
    @Operation(summary = "Met à jour un conducteur")
    @Auditable(action = "API_UPDATE_AUTO_DRIVER")
    @TenantRequired
    public ResponseEntity<DriverResponse> updateDriver(
            @PathVariable UUID id,
            @Valid @RequestBody CreateDriverRequest request,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour mettre à jour le conducteur avec ID: {} pour l'organisation: {}",
                id, organizationId);

        Driver driver = driverApiMapper.toEntity(request);
        DriverDTO driverDTO = driverService.updateDriver(id, driver, organizationId)
                .orElseThrow(() -> com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException
                        .forId("Conducteur", id));
        DriverResponse response = driverApiMapper.toResponse(driverDTO);

        return ResponseEntity.ok(response);
    }

    /**
     * Supprime un conducteur.
     *
     * @param id             L'ID du conducteur
     * @param organizationId L'ID de l'organisation
     * @return Réponse vide avec statut 204 No Content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime un conducteur")
    @Auditable(action = "API_DELETE_AUTO_DRIVER")
    @TenantRequired
    public ResponseEntity<Void> deleteDriver(
            @PathVariable UUID id,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour supprimer le conducteur avec ID: {} pour l'organisation: {}",
                id, organizationId);

        boolean deleted = driverService.deleteDriver(id, organizationId);
        if (!deleted) {
            throw com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException
                    .forId("Conducteur", id);
        }

        return ResponseEntity.noContent().build();
    }


}
