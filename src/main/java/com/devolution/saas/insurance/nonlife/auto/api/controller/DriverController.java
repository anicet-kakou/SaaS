package com.devolution.saas.insurance.nonlife.auto.api.controller;

import com.devolution.saas.insurance.nonlife.auto.api.request.CreateDriverRequest;
import com.devolution.saas.insurance.nonlife.auto.application.dto.DriverDTO;
import com.devolution.saas.insurance.nonlife.auto.application.service.DriverService;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Driver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des conducteurs.
 *
 * @deprecated Utiliser {@link AutoDriverController} à la place.
 * Ce contrôleur sera supprimé dans une version future.
 */
@Deprecated(since = "1.0.0", forRemoval = true)
@RestController
@RequestMapping("/api/v1/organizations/{organizationId}/drivers")
@RequiredArgsConstructor
@Slf4j
public class DriverController {

    private final DriverService driverService;

    /**
     * Crée un nouveau conducteur.
     *
     * @param organizationId L'ID de l'organisation
     * @param request        La requête de création
     * @return Le conducteur créé
     */
    @PostMapping
    public ResponseEntity<DriverDTO> createDriver(
            @PathVariable UUID organizationId,
            @Valid @RequestBody CreateDriverRequest request) {
        log.info("Création d'un nouveau conducteur pour l'organisation: {}", organizationId);

        Driver driver = mapRequestToEntity(request);
        DriverDTO createdDriver = driverService.createDriver(driver, organizationId);

        return new ResponseEntity<>(createdDriver, HttpStatus.CREATED);
    }

    /**
     * Récupère un conducteur par son ID.
     *
     * @param organizationId L'ID de l'organisation
     * @param id             L'ID du conducteur
     * @return Le conducteur trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriverById(
            @PathVariable UUID organizationId,
            @PathVariable UUID id) {
        log.info("Récupération du conducteur avec ID: {} pour l'organisation: {}", id, organizationId);

        DriverDTO driver = driverService.getDriverById(id, organizationId)
                .orElseThrow(() -> com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException.forId("Conducteur", id));

        return ResponseEntity.ok(driver);
    }

    /**
     * Récupère un conducteur par son numéro de permis.
     *
     * @param organizationId L'ID de l'organisation
     * @param licenseNumber  Le numéro de permis
     * @return Le conducteur trouvé
     */
    @GetMapping("/license/{licenseNumber}")
    public ResponseEntity<DriverDTO> getDriverByLicenseNumber(
            @PathVariable UUID organizationId,
            @PathVariable String licenseNumber) {
        log.info("Récupération du conducteur avec numéro de permis: {} pour l'organisation: {}", licenseNumber, organizationId);

        DriverDTO driver = driverService.getDriverByLicenseNumber(licenseNumber, organizationId)
                .orElseThrow(() -> com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException.forIdentifier("Conducteur", "numéro de permis", licenseNumber));

        return ResponseEntity.ok(driver);
    }

    /**
     * Liste tous les conducteurs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des conducteurs
     */
    @GetMapping
    public ResponseEntity<List<DriverDTO>> getAllDrivers(
            @PathVariable UUID organizationId) {
        log.info("Listage de tous les conducteurs pour l'organisation: {}", organizationId);

        List<DriverDTO> drivers = driverService.getAllDrivers(organizationId);
        return ResponseEntity.ok(drivers);
    }

    /**
     * Liste tous les conducteurs d'un client.
     *
     * @param organizationId L'ID de l'organisation
     * @param customerId     L'ID du client
     * @return La liste des conducteurs
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<DriverDTO>> getDriversByCustomer(
            @PathVariable UUID organizationId,
            @PathVariable UUID customerId) {
        log.info("Listage des conducteurs pour le client: {} dans l'organisation: {}", customerId, organizationId);

        List<DriverDTO> drivers = driverService.getDriversByCustomer(customerId, organizationId);
        return ResponseEntity.ok(drivers);
    }

    /**
     * Récupère le conducteur principal d'un client.
     *
     * @param organizationId L'ID de l'organisation
     * @param customerId     L'ID du client
     * @return Le conducteur principal
     */
    @GetMapping("/customer/{customerId}/primary")
    public ResponseEntity<DriverDTO> getPrimaryDriverByCustomer(
            @PathVariable UUID organizationId,
            @PathVariable UUID customerId) {
        log.info("Récupération du conducteur principal pour le client: {} dans l'organisation: {}", customerId, organizationId);

        DriverDTO driver = driverService.getPrimaryDriverByCustomer(customerId, organizationId)
                .orElseThrow(() -> com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException.forIdentifier("Conducteur principal", "client", customerId.toString()));

        return ResponseEntity.ok(driver);
    }

    /**
     * Met à jour un conducteur.
     *
     * @param organizationId L'ID de l'organisation
     * @param id             L'ID du conducteur
     * @param request        La requête de mise à jour
     * @return Le conducteur mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<DriverDTO> updateDriver(
            @PathVariable UUID organizationId,
            @PathVariable UUID id,
            @Valid @RequestBody CreateDriverRequest request) {
        log.info("Mise à jour du conducteur avec ID: {} pour l'organisation: {}", id, organizationId);

        Driver driver = mapRequestToEntity(request);
        DriverDTO updatedDriver = driverService.updateDriver(id, driver, organizationId)
                .orElseThrow(() -> com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException.forId("Conducteur", id));

        return ResponseEntity.ok(updatedDriver);
    }

    /**
     * Supprime un conducteur.
     *
     * @param organizationId L'ID de l'organisation
     * @param id             L'ID du conducteur
     * @return Réponse vide avec statut 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(
            @PathVariable UUID organizationId,
            @PathVariable UUID id) {
        log.info("Suppression du conducteur avec ID: {} pour l'organisation: {}", id, organizationId);

        boolean deleted = driverService.deleteDriver(id, organizationId);
        if (!deleted) {
            throw com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException.forId("Conducteur", id);
        }

        return ResponseEntity.noContent().build();
    }

    /**
     * Convertit une requête en entité.
     *
     * @param request La requête à convertir
     * @return L'entité correspondante
     */
    private Driver mapRequestToEntity(CreateDriverRequest request) {
        // Créer l'objet Driver avec le builder
        Driver driver = Driver.builder()
                .customerId(request.getCustomerId())
                .licenseNumber(request.getLicenseNumber())
                .licenseIssueDate(request.getLicenseIssueDate())
                .licenseExpiryDate(request.getLicenseExpiryDate())
                .isPrimaryDriver(request.isPrimaryDriver())
                .yearsOfDrivingExperience(request.getYearsOfDrivingExperience())
                .build();

        // Utiliser les setters pour définir les IDs
        driver.setLicenseTypeId(request.getLicenseTypeId());

        return driver;
    }
}
