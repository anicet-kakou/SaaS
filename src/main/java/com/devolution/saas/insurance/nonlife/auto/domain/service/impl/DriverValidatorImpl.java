package com.devolution.saas.insurance.nonlife.auto.domain.service.impl;

import com.devolution.saas.insurance.nonlife.auto.domain.model.Driver;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.DriverRepository;
import com.devolution.saas.insurance.nonlife.auto.domain.service.DriverValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implémentation du service de validation des conducteurs.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DriverValidatorImpl implements DriverValidator {

    private final DriverRepository driverRepository;

    @Override
    public List<String> validateForCreation(Driver driver, UUID organizationId) {
        log.debug("Validation d'un conducteur pour création: {}", driver.getLicenseNumber());

        List<String> errors = new ArrayList<>();

        // Validation des champs obligatoires
        if (driver.getCustomerId() == null) {
            errors.add("L'ID du client est obligatoire");
        }

        if (driver.getLicenseNumber() == null || driver.getLicenseNumber().isBlank()) {
            errors.add("Le numéro de permis est obligatoire");
        }

        if (driver.getLicenseTypeId() == null) {
            errors.add("Le type de permis est obligatoire");
        }

        if (driver.getLicenseIssueDate() == null) {
            errors.add("La date de délivrance du permis est obligatoire");
        }

        // Validation des références
        errors.addAll(validateReferences(driver, organizationId));

        // Validation des règles métier
        errors.addAll(validateBusinessRules(driver));

        // Vérification de l'unicité du numéro de permis
        if (driver.getLicenseNumber() != null && !driver.getLicenseNumber().isBlank()) {
            driverRepository.findByLicenseNumberAndOrganizationId(driver.getLicenseNumber(), organizationId)
                    .ifPresent(existingDriver -> errors.add("Un conducteur avec ce numéro de permis existe déjà"));
        }

        return errors;
    }

    @Override
    public List<String> validateForUpdate(Driver driver, Driver existingDriver, UUID organizationId) {
        log.debug("Validation d'un conducteur pour mise à jour: {}", driver.getLicenseNumber());

        List<String> errors = new ArrayList<>();

        // Validation des champs obligatoires (comme pour la création)
        if (driver.getCustomerId() == null) {
            errors.add("L'ID du client est obligatoire");
        }

        if (driver.getLicenseNumber() == null || driver.getLicenseNumber().isBlank()) {
            errors.add("Le numéro de permis est obligatoire");
        }

        if (driver.getLicenseTypeId() == null) {
            errors.add("Le type de permis est obligatoire");
        }

        if (driver.getLicenseIssueDate() == null) {
            errors.add("La date de délivrance du permis est obligatoire");
        }

        // Validation des références
        errors.addAll(validateReferences(driver, organizationId));

        // Validation des règles métier
        errors.addAll(validateBusinessRules(driver));

        // Validation spécifique à la mise à jour
        if (!existingDriver.getLicenseNumber().equals(driver.getLicenseNumber())) {
            // Si le numéro de permis a changé, vérifier qu'il n'est pas déjà utilisé
            driverRepository.findByLicenseNumberAndOrganizationId(driver.getLicenseNumber(), organizationId)
                    .ifPresent(otherDriver -> {
                        if (!otherDriver.getId().equals(driver.getId())) {
                            errors.add("Un conducteur avec ce numéro de permis existe déjà");
                        }
                    });
        }

        return errors;
    }

    @Override
    public List<String> validateReferences(Driver driver, UUID organizationId) {
        log.debug("Validation des références pour le conducteur: {}", driver.getLicenseNumber());

        List<String> errors = new ArrayList<>();

        // Pas de références externes à valider pour le conducteur dans cette implémentation
        // Si nécessaire, ajouter ici la validation des références (par exemple, vérifier l'existence du client)

        return errors;
    }

    @Override
    public List<String> validateBusinessRules(Driver driver) {
        log.debug("Validation des règles métier pour le conducteur: {}", driver.getLicenseNumber());

        List<String> errors = new ArrayList<>();

        // Validation du numéro de permis (format)
        if (driver.getLicenseNumber() != null &&
                !driver.getLicenseNumber().matches("^[A-Z0-9-]+$")) {
            errors.add("Le numéro de permis n'est pas dans un format valide");
        }

        // Validation des dates
        if (driver.getLicenseIssueDate() != null) {
            // La date de délivrance ne doit pas être dans le futur
            if (driver.getLicenseIssueDate().isAfter(LocalDate.now())) {
                errors.add("La date de délivrance du permis ne peut pas être dans le futur");
            }

            // La date de délivrance ne doit pas être trop ancienne (plus de 100 ans)
            if (driver.getLicenseIssueDate().isBefore(LocalDate.now().minusYears(100))) {
                errors.add("La date de délivrance du permis est trop ancienne");
            }

            // Si date d'expiration présente, elle doit être après la date de délivrance
            if (driver.getLicenseExpiryDate() != null &&
                    driver.getLicenseExpiryDate().isBefore(driver.getLicenseIssueDate())) {
                errors.add("La date d'expiration du permis doit être postérieure à la date de délivrance");
            }

            // Si date d'expiration présente, elle ne doit pas être dans le passé
            if (driver.getLicenseExpiryDate() != null &&
                    driver.getLicenseExpiryDate().isBefore(LocalDate.now())) {
                errors.add("Le permis est expiré");
            }
        }

        // Validation de l'expérience de conduite
        if (driver.getLicenseIssueDate() != null) {
            int yearsOfExperience = Period.between(driver.getLicenseIssueDate(), LocalDate.now()).getYears();
            if (driver.getYearsOfDrivingExperience() > yearsOfExperience) {
                errors.add("L'expérience de conduite ne peut pas être supérieure à l'âge du permis");
            }
        }

        if (driver.getYearsOfDrivingExperience() < 0) {
            errors.add("L'expérience de conduite ne peut pas être négative");
        }

        return errors;
    }
}
