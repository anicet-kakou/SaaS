package com.devolution.saas.insurance.nonlife.auto.domain.service.impl;

import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;
import com.devolution.saas.insurance.nonlife.auto.domain.port.DriverProvider;
import com.devolution.saas.insurance.nonlife.auto.domain.port.PolicyProvider;
import com.devolution.saas.insurance.nonlife.auto.domain.port.VehicleProvider;
import com.devolution.saas.insurance.nonlife.auto.domain.service.PolicyValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implémentation du service de validation des polices d'assurance automobile.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyValidatorImpl implements PolicyValidator {

    private final VehicleProvider vehicleProvider;
    private final DriverProvider driverProvider;
    private final PolicyProvider policyProvider;

    @Override
    public List<String> validateForCreation(AutoPolicy policy, UUID organizationId) {
        log.debug("Validation d'une police pour création: {}", policy.getPolicyNumber());

        List<String> errors = new ArrayList<>();

        // Validation des champs obligatoires
        if (policy.getPolicyNumber() == null || policy.getPolicyNumber().isBlank()) {
            errors.add("Le numéro de police est obligatoire");
        }

        if (policy.getStatus() == null) {
            errors.add("Le statut de la police est obligatoire");
        }

        if (policy.getStartDate() == null) {
            errors.add("La date de début de la police est obligatoire");
        }

        if (policy.getEndDate() == null) {
            errors.add("La date de fin de la police est obligatoire");
        }

        if (policy.getPremiumAmount() == null) {
            errors.add("Le montant de la prime est obligatoire");
        }

        if (policy.getVehicleId() == null) {
            errors.add("Le véhicule est obligatoire");
        }

        if (policy.getPrimaryDriverId() == null) {
            errors.add("Le conducteur principal est obligatoire");
        }

        if (policy.getCoverageType() == null) {
            errors.add("Le type de couverture est obligatoire");
        }

        if (policy.getBonusMalusCoefficient() == null) {
            errors.add("Le coefficient de bonus-malus est obligatoire");
        }

        if (policy.getClaimHistoryCategoryId() == null) {
            errors.add("La catégorie d'historique de sinistres est obligatoire");
        }

        // Validation des références
        errors.addAll(validateReferences(policy, organizationId));

        // Validation des règles métier
        errors.addAll(validateBusinessRules(policy));

        // Vérification de l'unicité du numéro de police
        if (policy.getPolicyNumber() != null && !policy.getPolicyNumber().isBlank()) {
            if (policyProvider.policyNumberExists(policy.getPolicyNumber(), organizationId)) {
                errors.add("Une police avec ce numéro existe déjà");
            }
        }

        return errors;
    }

    @Override
    public List<String> validateForUpdate(AutoPolicy policy, AutoPolicy existingPolicy, UUID organizationId) {
        log.debug("Validation d'une police pour mise à jour: {}", policy.getPolicyNumber());

        List<String> errors = new ArrayList<>();

        // Validation des champs obligatoires (comme pour la création)
        if (policy.getPolicyNumber() == null || policy.getPolicyNumber().isBlank()) {
            errors.add("Le numéro de police est obligatoire");
        }

        if (policy.getStatus() == null) {
            errors.add("Le statut de la police est obligatoire");
        }

        if (policy.getStartDate() == null) {
            errors.add("La date de début de la police est obligatoire");
        }

        if (policy.getEndDate() == null) {
            errors.add("La date de fin de la police est obligatoire");
        }

        if (policy.getPremiumAmount() == null) {
            errors.add("Le montant de la prime est obligatoire");
        }

        if (policy.getVehicleId() == null) {
            errors.add("Le véhicule est obligatoire");
        }

        if (policy.getPrimaryDriverId() == null) {
            errors.add("Le conducteur principal est obligatoire");
        }

        if (policy.getCoverageType() == null) {
            errors.add("Le type de couverture est obligatoire");
        }

        if (policy.getBonusMalusCoefficient() == null) {
            errors.add("Le coefficient de bonus-malus est obligatoire");
        }

        if (policy.getClaimHistoryCategoryId() == null) {
            errors.add("La catégorie d'historique de sinistres est obligatoire");
        }

        // Validation des références
        errors.addAll(validateReferences(policy, organizationId));

        // Validation des règles métier
        errors.addAll(validateBusinessRules(policy));

        // Validation spécifique à la mise à jour
        if (!existingPolicy.getPolicyNumber().equals(policy.getPolicyNumber())) {
            errors.add("Le numéro de police ne peut pas être modifié");
        }

        // Vérification de la cohérence des dates
        if (existingPolicy.getStartDate().isAfter(policy.getStartDate())) {
            errors.add("La nouvelle date de début ne peut pas être antérieure à la date de début originale");
        }

        return errors;
    }

    @Override
    public List<String> validateReferences(AutoPolicy policy, UUID organizationId) {
        log.debug("Validation des références pour la police: {}", policy.getPolicyNumber());

        List<String> errors = new ArrayList<>();

        // Vérification de l'existence du véhicule
        if (policy.getVehicleId() != null &&
                !vehicleProvider.vehicleExists(policy.getVehicleId(), organizationId)) {
            errors.add("Le véhicule spécifié n'existe pas");
        }

        // Vérification de l'existence du conducteur principal
        if (policy.getPrimaryDriverId() != null &&
                !driverProvider.driverExists(policy.getPrimaryDriverId(), organizationId)) {
            errors.add("Le conducteur principal spécifié n'existe pas");
        }

        return errors;
    }

    @Override
    public List<String> validateBusinessRules(AutoPolicy policy) {
        log.debug("Validation des règles métier pour la police: {}", policy.getPolicyNumber());

        List<String> errors = new ArrayList<>();

        // Validation du numéro de police (format)
        if (policy.getPolicyNumber() != null &&
                !policy.getPolicyNumber().matches("^[A-Z0-9-]+$")) {
            errors.add("Le numéro de police n'est pas dans un format valide");
        }

        // Validation des dates
        if (policy.getStartDate() != null && policy.getEndDate() != null) {
            // La date de début doit être antérieure à la date de fin
            if (policy.getStartDate().isAfter(policy.getEndDate())) {
                errors.add("La date de début doit être antérieure à la date de fin");
            }

            // La durée de la police ne doit pas dépasser 1 an
            if (policy.getStartDate().plusYears(1).isBefore(policy.getEndDate())) {
                errors.add("La durée de la police ne peut pas dépasser 1 an");
            }

            // La date de début ne doit pas être dans le passé (sauf pour les renouvellements)
            if (policy.getStartDate().isBefore(LocalDate.now().minusDays(30))) {
                errors.add("La date de début ne peut pas être antérieure à 30 jours");
            }
        }

        // Validation du montant de la prime
        if (policy.getPremiumAmount() != null && policy.getPremiumAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Le montant de la prime doit être positif");
        }

        // Validation du coefficient de bonus-malus
        if (policy.getBonusMalusCoefficient() != null) {
            if (policy.getBonusMalusCoefficient().compareTo(new BigDecimal("0.5")) < 0) {
                errors.add("Le coefficient de bonus-malus ne peut pas être inférieur à 0.5");
            }
            if (policy.getBonusMalusCoefficient().compareTo(new BigDecimal("3.5")) > 0) {
                errors.add("Le coefficient de bonus-malus ne peut pas être supérieur à 3.5");
            }
        }

        // Validation du kilométrage annuel
        if (policy.getAnnualMileage() != null && policy.getAnnualMileage() < 0) {
            errors.add("Le kilométrage annuel ne peut pas être négatif");
        }

        return errors;
    }
}
