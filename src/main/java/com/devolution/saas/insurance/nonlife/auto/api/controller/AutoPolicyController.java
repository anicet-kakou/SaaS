package com.devolution.saas.insurance.nonlife.auto.api.controller;

import com.devolution.saas.insurance.nonlife.auto.api.dto.request.CreateAutoPolicyRequest;
import com.devolution.saas.insurance.nonlife.auto.application.dto.AutoPolicyDTO;
import com.devolution.saas.insurance.nonlife.auto.application.service.AutoPolicyService;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des polices d'assurance automobile.
 */
@RestController
@RequestMapping("/api/v1/organizations/{organizationId}/auto-policies")
@RequiredArgsConstructor
@Slf4j
public class AutoPolicyController {

    private final AutoPolicyService autoPolicyService;

    /**
     * Crée une nouvelle police d'assurance automobile.
     *
     * @param organizationId L'ID de l'organisation
     * @param request        La requête de création
     * @return La police créée
     */
    @PostMapping
    public ResponseEntity<AutoPolicyDTO> createPolicy(
            @PathVariable UUID organizationId,
            @Valid @RequestBody CreateAutoPolicyRequest request) {
        log.info("Création d'une nouvelle police d'assurance automobile pour l'organisation: {}", organizationId);

        AutoPolicy policy = mapRequestToEntity(request);
        AutoPolicyDTO createdPolicy = autoPolicyService.createPolicy(policy, organizationId);

        return new ResponseEntity<>(createdPolicy, HttpStatus.CREATED);
    }

    /**
     * Récupère une police par son ID.
     *
     * @param organizationId L'ID de l'organisation
     * @param id             L'ID de la police
     * @return La police trouvée
     */
    @GetMapping("/{id}")
    public ResponseEntity<AutoPolicyDTO> getPolicyById(
            @PathVariable UUID organizationId,
            @PathVariable UUID id) {
        log.info("Récupération de la police d'assurance automobile avec ID: {} pour l'organisation: {}", id, organizationId);

        AutoPolicyDTO policy = autoPolicyService.getPolicyById(id, organizationId)
                .orElseThrow(() -> com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException.forId("Police d'assurance automobile", id));

        return ResponseEntity.ok(policy);
    }

    /**
     * Récupère une police par son numéro.
     *
     * @param organizationId L'ID de l'organisation
     * @param policyNumber   Le numéro de police
     * @return La police trouvée
     */
    @GetMapping("/number/{policyNumber}")
    public ResponseEntity<AutoPolicyDTO> getPolicyByNumber(
            @PathVariable UUID organizationId,
            @PathVariable String policyNumber) {
        log.info("Récupération de la police d'assurance automobile avec numéro: {} pour l'organisation: {}", policyNumber, organizationId);

        AutoPolicyDTO policy = autoPolicyService.getPolicyByNumber(policyNumber, organizationId)
                .orElseThrow(() -> com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException.forIdentifier("Police d'assurance automobile", "numéro", policyNumber));

        return ResponseEntity.ok(policy);
    }

    /**
     * Liste toutes les polices d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des polices
     */
    @GetMapping
    public ResponseEntity<List<AutoPolicyDTO>> getAllPolicies(
            @PathVariable UUID organizationId) {
        log.info("Listage de toutes les polices d'assurance automobile pour l'organisation: {}", organizationId);

        List<AutoPolicyDTO> policies = autoPolicyService.getAllPolicies(organizationId);
        return ResponseEntity.ok(policies);
    }

    /**
     * Liste toutes les polices actives d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des polices actives
     */
    @GetMapping("/active")
    public ResponseEntity<List<AutoPolicyDTO>> getAllActivePolicies(
            @PathVariable UUID organizationId) {
        log.info("Listage de toutes les polices d'assurance automobile actives pour l'organisation: {}", organizationId);

        List<AutoPolicyDTO> policies = autoPolicyService.getAllActivePolicies(organizationId);
        return ResponseEntity.ok(policies);
    }

    /**
     * Liste toutes les polices d'un véhicule.
     *
     * @param organizationId L'ID de l'organisation
     * @param vehicleId      L'ID du véhicule
     * @return La liste des polices
     */
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<AutoPolicyDTO>> getPoliciesByVehicle(
            @PathVariable UUID organizationId,
            @PathVariable UUID vehicleId) {
        log.info("Listage des polices d'assurance automobile pour le véhicule: {} dans l'organisation: {}", vehicleId, organizationId);

        List<AutoPolicyDTO> policies = autoPolicyService.getPoliciesByVehicle(vehicleId, organizationId);
        return ResponseEntity.ok(policies);
    }

    /**
     * Liste toutes les polices d'un conducteur principal.
     *
     * @param organizationId L'ID de l'organisation
     * @param driverId       L'ID du conducteur
     * @return La liste des polices
     */
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<AutoPolicyDTO>> getPoliciesByPrimaryDriver(
            @PathVariable UUID organizationId,
            @PathVariable UUID driverId) {
        log.info("Listage des polices d'assurance automobile pour le conducteur principal: {} dans l'organisation: {}", driverId, organizationId);

        List<AutoPolicyDTO> policies = autoPolicyService.getPoliciesByPrimaryDriver(driverId, organizationId);
        return ResponseEntity.ok(policies);
    }

    /**
     * Liste toutes les polices qui expirent entre deux dates.
     *
     * @param organizationId L'ID de l'organisation
     * @param startDate      La date de début
     * @param endDate        La date de fin
     * @return La liste des polices
     */
    @GetMapping("/expiring")
    public ResponseEntity<List<AutoPolicyDTO>> getPoliciesExpiringBetween(
            @PathVariable UUID organizationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("Listage des polices d'assurance automobile expirant entre: {} et {} pour l'organisation: {}", startDate, endDate, organizationId);

        List<AutoPolicyDTO> policies = autoPolicyService.getPoliciesExpiringBetween(startDate, endDate, organizationId);
        return ResponseEntity.ok(policies);
    }

    /**
     * Met à jour une police.
     *
     * @param organizationId L'ID de l'organisation
     * @param id             L'ID de la police
     * @param request        La requête de mise à jour
     * @return La police mise à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<AutoPolicyDTO> updatePolicy(
            @PathVariable UUID organizationId,
            @PathVariable UUID id,
            @Valid @RequestBody CreateAutoPolicyRequest request) {
        log.info("Mise à jour de la police d'assurance automobile avec ID: {} pour l'organisation: {}", id, organizationId);

        AutoPolicy policy = mapRequestToEntity(request);
        AutoPolicyDTO updatedPolicy = autoPolicyService.updatePolicy(id, policy, organizationId)
                .orElseThrow(() -> com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException.forId("Police d'assurance automobile", id));

        return ResponseEntity.ok(updatedPolicy);
    }

    /**
     * Supprime une police.
     *
     * @param organizationId L'ID de l'organisation
     * @param id             L'ID de la police
     * @return Réponse vide avec statut 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePolicy(
            @PathVariable UUID organizationId,
            @PathVariable UUID id) {
        log.info("Suppression de la police d'assurance automobile avec ID: {} pour l'organisation: {}", id, organizationId);

        boolean deleted = autoPolicyService.deletePolicy(id, organizationId);
        if (!deleted) {
            throw com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException.forId("Police d'assurance automobile", id);
        }

        return ResponseEntity.noContent().build();
    }

    /**
     * Convertit une requête en entité.
     *
     * @param request La requête à convertir
     * @return L'entité correspondante
     */
    private AutoPolicy mapRequestToEntity(CreateAutoPolicyRequest request) {
        // Créer l'objet AutoPolicy avec le builder
        AutoPolicy policy = AutoPolicy.builder()
                .policyNumber(request.policyNumber())
                .status(AutoPolicy.PolicyStatus.ACTIVE) // Par défaut, la police est active
                .startDate(request.startDate())
                .endDate(request.endDate())
                .premiumAmount(request.premiumAmount())
                .coverageType(request.coverageType())
                .bonusMalusCoefficient(request.bonusMalusCoefficient())
                .annualMileage(request.annualMileage())
                .parkingType(request.parkingType())
                .hasAntiTheftDevice(request.hasAntiTheftDevice())
                .claimHistoryCategoryId(request.claimHistoryCategoryId())
                .build();

        // Utiliser les setters pour définir les IDs
        // Note: Ces setters ne font rien actuellement, mais sont nécessaires pour la compatibilité
        policy.setVehicleId(request.vehicleId());
        policy.setPrimaryDriverId(request.primaryDriverId());

        return policy;
    }
}
