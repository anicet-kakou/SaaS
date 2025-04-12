package com.devolution.saas.insurance.nonlife.auto.domain.service.impl;

import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Driver;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.AutoPolicyRepository;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.DriverRepository;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.VehicleRepository;
import com.devolution.saas.insurance.nonlife.auto.domain.service.AutoPolicyValidator;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.CirculationZoneRepository;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.GeographicZoneRepository;
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
public class AutoPolicyValidatorImpl implements AutoPolicyValidator {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final AutoPolicyRepository autoPolicyRepository;
    private final GeographicZoneRepository geographicZoneRepository;
    private final CirculationZoneRepository circulationZoneRepository;

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

        // Validation des zones géographiques et de circulation (conformément au Code CIMA)
        if (policy.getGeographicZone() == null || policy.getGeographicZoneId() == null) {
            errors.add("La zone géographique de résidence est obligatoire selon le Code CIMA");
        }

        if (policy.getCirculationZone() == null || policy.getCirculationZoneId() == null) {
            errors.add("La zone de circulation est obligatoire selon le Code CIMA");
        }

        // Validation des références
        errors.addAll(validateReferences(policy, organizationId));

        // Validation des règles métier
        errors.addAll(validateBusinessRules(policy));

        // Validation des règles spécifiques à l'auto
        errors.addAll(validateAutoSpecificRules(policy));

        // Validation du véhicule
        errors.addAll(validateVehicle(policy, organizationId));

        // Validation du conducteur
        errors.addAll(validateDriver(policy, organizationId));

        // Validation des dates
        errors.addAll(validateDates(policy));

        // Validation de la prime
        errors.addAll(validatePremium(policy));

        // Validation des garanties
        errors.addAll(validateCoverages(policy, organizationId));

        // Vérification de l'unicité du numéro de police
        if (policy.getPolicyNumber() != null && !policy.getPolicyNumber().isBlank()) {
            autoPolicyRepository.findByPolicyNumberAndOrganizationId(policy.getPolicyNumber(), organizationId)
                    .ifPresent(existingPolicy -> errors.add("Une police avec ce numéro existe déjà"));
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

        // Validation des zones géographiques et de circulation (conformément au Code CIMA)
        if (policy.getGeographicZone() == null || policy.getGeographicZoneId() == null) {
            errors.add("La zone géographique de résidence est obligatoire selon le Code CIMA");
        }

        if (policy.getCirculationZone() == null || policy.getCirculationZoneId() == null) {
            errors.add("La zone de circulation est obligatoire selon le Code CIMA");
        }

        // Validation des références
        errors.addAll(validateReferences(policy, organizationId));

        // Validation des règles métier
        errors.addAll(validateBusinessRules(policy));

        // Validation des règles spécifiques à l'auto
        errors.addAll(validateAutoSpecificRules(policy));

        // Validation du véhicule
        errors.addAll(validateVehicle(policy, organizationId));

        // Validation du conducteur
        errors.addAll(validateDriver(policy, organizationId));

        // Validation des dates
        errors.addAll(validateDates(policy));

        // Validation de la prime
        errors.addAll(validatePremium(policy));

        // Validation des garanties
        errors.addAll(validateCoverages(policy, organizationId));

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
                !vehicleRepository.findById(policy.getVehicleId())
                        .filter(vehicle -> vehicle.getOrganizationId().equals(organizationId))
                        .isPresent()) {
            errors.add("Le véhicule spécifié n'existe pas");
        }

        // Vérification de l'existence du conducteur principal
        if (policy.getPrimaryDriverId() != null &&
                !driverRepository.findById(policy.getPrimaryDriverId())
                        .filter(driver -> driver.getOrganizationId().equals(organizationId))
                        .isPresent()) {
            errors.add("Le conducteur principal spécifié n'existe pas");
        }

        // Vérification de l'existence de la zone géographique
        if (policy.getGeographicZoneId() != null &&
                !geographicZoneRepository.findById(policy.getGeographicZoneId())
                        .filter(zone -> zone.getOrganizationId().equals(organizationId))
                        .isPresent()) {
            errors.add("La zone géographique spécifiée n'existe pas");
        }

        // Vérification de l'existence de la zone de circulation
        if (policy.getCirculationZoneId() != null &&
                !circulationZoneRepository.findById(policy.getCirculationZoneId())
                        .filter(zone -> zone.getOrganizationId().equals(organizationId))
                        .isPresent()) {
            errors.add("La zone de circulation spécifiée n'existe pas");
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

    @Override
    public List<String> validateAutoSpecificRules(AutoPolicy policy) {
        log.debug("Validation des règles spécifiques à l'auto pour la police: {}", policy.getPolicyNumber());

        List<String> errors = new ArrayList<>();

        // Validation du type de couverture
        if (policy.getCoverageType() != null) {
            // Règles spécifiques selon le type de couverture
            switch (policy.getCoverageType()) {
                case THIRD_PARTY:
                    // Pas de règles spécifiques pour la responsabilité civile
                    break;
                case COMPREHENSIVE:
                    // Pour tous risques, vérifier l'âge du véhicule
                    Vehicle vehicle = vehicleRepository.findById(policy.getVehicleId()).orElse(null);
                    if (vehicle != null && (LocalDate.now().getYear() - vehicle.getYear()) > 15) {
                        errors.add("La couverture tous risques n'est pas disponible pour les véhicules de plus de 15 ans");
                    }
                    break;
                default:
                    errors.add("Type de couverture non reconnu");
            }
        }

        // Validation du type de stationnement
        if (policy.getParkingType() == null) {
            errors.add("Le type de stationnement est obligatoire");
        }

        return errors;
    }

    @Override
    public List<String> validateVehicle(AutoPolicy policy, UUID organizationId) {
        log.debug("Validation du véhicule pour la police: {}", policy.getPolicyNumber());

        List<String> errors = new ArrayList<>();

        if (policy.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findById(policy.getVehicleId())
                    .filter(v -> v.getOrganizationId().equals(organizationId))
                    .orElse(null);

            if (vehicle != null) {
                // Vérifier que le véhicule n'est pas trop ancien
                int vehicleAge = LocalDate.now().getYear() - vehicle.getYear();
                if (vehicleAge > 30) {
                    errors.add("Le véhicule est trop ancien pour être assuré (plus de 30 ans)");
                }

                // Vérifier que le véhicule a un numéro d'immatriculation valide
                if (vehicle.getRegistrationNumber() == null || vehicle.getRegistrationNumber().isBlank()) {
                    errors.add("Le véhicule doit avoir un numéro d'immatriculation valide");
                }
            }
        }

        return errors;
    }

    @Override
    public List<String> validateDriver(AutoPolicy policy, UUID organizationId) {
        log.debug("Validation du conducteur pour la police: {}", policy.getPolicyNumber());

        List<String> errors = new ArrayList<>();

        if (policy.getPrimaryDriverId() != null) {
            Driver driver = driverRepository.findById(policy.getPrimaryDriverId())
                    .filter(d -> d.getOrganizationId().equals(organizationId))
                    .orElse(null);

            if (driver != null) {
                // Vérifier que le conducteur a un permis valide
                if (driver.getLicenseExpiryDate() != null && driver.getLicenseExpiryDate().isBefore(LocalDate.now())) {
                    errors.add("Le permis du conducteur principal est expiré");
                }

                // Vérifier l'expérience de conduite
                if (driver.getYearsOfDrivingExperience() < 2) {
                    errors.add("Le conducteur principal doit avoir au moins 2 ans d'expérience de conduite");
                }
            }
        }

        return errors;
    }

    @Override
    public List<String> validateDates(AutoPolicy policy) {
        log.debug("Validation des dates pour la police: {}", policy.getPolicyNumber());

        List<String> errors = new ArrayList<>();

        if (policy.getStartDate() != null && policy.getEndDate() != null) {
            // La date de début doit être antérieure à la date de fin
            if (policy.getStartDate().isAfter(policy.getEndDate())) {
                errors.add("La date de début doit être antérieure à la date de fin");
            }

            // La durée de la police doit être d'au moins 1 mois
            if (policy.getStartDate().plusMonths(1).isAfter(policy.getEndDate())) {
                errors.add("La durée de la police doit être d'au moins 1 mois");
            }

            // La durée de la police ne doit pas dépasser 1 an
            if (policy.getStartDate().plusYears(1).isBefore(policy.getEndDate())) {
                errors.add("La durée de la police ne peut pas dépasser 1 an");
            }
        }

        return errors;
    }

    @Override
    public List<String> validatePremium(AutoPolicy policy) {
        log.debug("Validation de la prime pour la police: {}", policy.getPolicyNumber());

        List<String> errors = new ArrayList<>();

        if (policy.getPremiumAmount() != null) {
            // La prime doit être positive
            if (policy.getPremiumAmount().compareTo(BigDecimal.ZERO) <= 0) {
                errors.add("Le montant de la prime doit être positif");
            }

            // La prime ne doit pas être trop élevée (limite arbitraire)
            if (policy.getPremiumAmount().compareTo(new BigDecimal("10000")) > 0) {
                errors.add("Le montant de la prime est anormalement élevé");
            }
        }

        return errors;
    }

    @Override
    public List<String> validateCoverages(AutoPolicy policy, UUID organizationId) {
        log.debug("Validation des garanties pour la police: {}", policy.getPolicyNumber());

        List<String> errors = new ArrayList<>();

        // Dans cette implémentation, nous n'avons pas encore de gestion des garanties
        // Cette méthode sera complétée lorsque nous implémenterons la gestion des garanties

        return errors;
    }
}
