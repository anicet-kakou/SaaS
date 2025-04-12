package com.devolution.saas.insurance.nonlife.auto.domain.service.impl;

import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Driver;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;
import com.devolution.saas.insurance.nonlife.auto.domain.service.PremiumCalculator;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleCategory;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleUsage;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleCategoryRepository;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleUsageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Implémentation du service de calcul de la prime d'assurance auto.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PremiumCalculatorImpl implements PremiumCalculator {

    private final VehicleCategoryRepository vehicleCategoryRepository;
    private final VehicleUsageRepository vehicleUsageRepository;

    @Override
    public BigDecimal calculateBasePremium(Vehicle vehicle, String coverageType, UUID organizationId) {
        log.debug("Calcul de la prime de base pour le véhicule: {}", vehicle.getRegistrationNumber());

        // Récupération de la catégorie du véhicule
        VehicleCategory category = vehicleCategoryRepository.findById(vehicle.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Catégorie de véhicule non trouvée"));

        // Récupération de l'usage du véhicule
        VehicleUsage usage = vehicleUsageRepository.findById(vehicle.getUsageId())
                .orElseThrow(() -> new IllegalArgumentException("Usage de véhicule non trouvé"));

        // Prime de base selon la catégorie
        BigDecimal basePremium = category.getTariffCoefficient().multiply(new BigDecimal("500"));

        // Ajustement selon l'usage
        basePremium = basePremium.multiply(usage.getTariffCoefficient());

        // Ajustement selon le type de couverture
        if ("COMPREHENSIVE".equals(coverageType)) {
            basePremium = basePremium.multiply(new BigDecimal("1.5"));
        }

        // Ajustement selon l'âge du véhicule
        int vehicleAge = LocalDate.now().getYear() - vehicle.getYear();
        if (vehicleAge < 3) {
            basePremium = basePremium.multiply(new BigDecimal("1.2"));
        } else if (vehicleAge > 10) {
            basePremium = basePremium.multiply(new BigDecimal("0.8"));
        }

        // Arrondi à 2 décimales
        return basePremium.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public Map<String, BigDecimal> calculateFactors(Vehicle vehicle, Driver driver, String coverageType, UUID organizationId) {
        log.debug("Calcul des facteurs pour le véhicule: {} et le conducteur: {}",
                vehicle.getRegistrationNumber(), driver.getLicenseNumber());

        Map<String, BigDecimal> factors = new HashMap<>();

        // Facteur lié à l'expérience du conducteur
        int drivingExperience = driver.getYearsOfDrivingExperience();
        if (drivingExperience < 2) {
            factors.put("experienceFactor", new BigDecimal("1.5"));
        } else if (drivingExperience < 5) {
            factors.put("experienceFactor", new BigDecimal("1.2"));
        } else if (drivingExperience < 10) {
            factors.put("experienceFactor", new BigDecimal("1.0"));
        } else {
            factors.put("experienceFactor", new BigDecimal("0.9"));
        }

        // Facteur lié à la puissance du moteur
        if (vehicle.getEnginePower() != null) {
            if (vehicle.getEnginePower() > 200) {
                factors.put("powerFactor", new BigDecimal("1.3"));
            } else if (vehicle.getEnginePower() > 150) {
                factors.put("powerFactor", new BigDecimal("1.2"));
            } else if (vehicle.getEnginePower() > 100) {
                factors.put("powerFactor", new BigDecimal("1.1"));
            } else {
                factors.put("powerFactor", new BigDecimal("1.0"));
            }
        } else {
            factors.put("powerFactor", new BigDecimal("1.0"));
        }

        // Facteur lié au kilométrage annuel
        if (vehicle.getMileage() != null) {
            if (vehicle.getMileage() > 30000) {
                factors.put("mileageFactor", new BigDecimal("1.2"));
            } else if (vehicle.getMileage() > 20000) {
                factors.put("mileageFactor", new BigDecimal("1.1"));
            } else if (vehicle.getMileage() > 10000) {
                factors.put("mileageFactor", new BigDecimal("1.0"));
            } else {
                factors.put("mileageFactor", new BigDecimal("0.9"));
            }
        } else {
            factors.put("mileageFactor", new BigDecimal("1.0"));
        }

        // Facteur lié à la présence d'un dispositif antivol
        if (vehicle.isHasAntiTheftDevice()) {
            factors.put("antiTheftFactor", new BigDecimal("0.95"));
        } else {
            factors.put("antiTheftFactor", new BigDecimal("1.0"));
        }

        // Facteur lié au type de stationnement
        if (vehicle.getParkingType() != null) {
            switch (vehicle.getParkingType()) {
                case GARAGE:
                    factors.put("parkingFactor", new BigDecimal("0.9"));
                    break;
                case PARKING_LOT:
                    factors.put("parkingFactor", new BigDecimal("0.95"));
                    break;
                case STREET:
                    factors.put("parkingFactor", new BigDecimal("1.1"));
                    break;
                default:
                    factors.put("parkingFactor", new BigDecimal("1.0"));
            }
        } else {
            factors.put("parkingFactor", new BigDecimal("1.0"));
        }

        return factors;
    }

    @Override
    public BigDecimal calculateAdjustedPremium(BigDecimal basePremium, Map<String, BigDecimal> factors) {
        log.debug("Calcul de la prime ajustée avec prime de base: {}", basePremium);

        BigDecimal adjustedPremium = basePremium;

        // Application de chaque facteur
        for (BigDecimal factor : factors.values()) {
            adjustedPremium = adjustedPremium.multiply(factor);
        }

        // Arrondi à 2 décimales
        return adjustedPremium.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateFinalPremium(BigDecimal adjustedPremium, BigDecimal bonusMalusCoefficient) {
        log.debug("Calcul de la prime finale avec prime ajustée: {} et coefficient bonus-malus: {}",
                adjustedPremium, bonusMalusCoefficient);

        // Application du coefficient de bonus-malus
        BigDecimal finalPremium = adjustedPremium.multiply(bonusMalusCoefficient);

        // Arrondi à 2 décimales
        return finalPremium.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public Map<String, BigDecimal> calculateCoverages(BigDecimal finalPremium, String coverageType, UUID organizationId) {
        log.debug("Calcul des garanties pour la prime finale: {} et type de couverture: {}",
                finalPremium, coverageType);

        Map<String, BigDecimal> coverages = new HashMap<>();

        if ("THIRD_PARTY".equals(coverageType)) {
            // Répartition pour la responsabilité civile
            coverages.put("responsabiliteCivile", finalPremium.multiply(new BigDecimal("0.8")));
            coverages.put("assistanceRoutiere", finalPremium.multiply(new BigDecimal("0.1")));
            coverages.put("protectionJuridique", finalPremium.multiply(new BigDecimal("0.1")));
        } else if ("COMPREHENSIVE".equals(coverageType)) {
            // Répartition pour tous risques
            coverages.put("responsabiliteCivile", finalPremium.multiply(new BigDecimal("0.5")));
            coverages.put("dommagesTousAccidents", finalPremium.multiply(new BigDecimal("0.25")));
            coverages.put("vol", finalPremium.multiply(new BigDecimal("0.1")));
            coverages.put("incendie", finalPremium.multiply(new BigDecimal("0.05")));
            coverages.put("brisDeGlace", finalPremium.multiply(new BigDecimal("0.05")));
            coverages.put("assistanceRoutiere", finalPremium.multiply(new BigDecimal("0.025")));
            coverages.put("protectionJuridique", finalPremium.multiply(new BigDecimal("0.025")));
        }

        // Arrondi à 2 décimales pour chaque garantie
        for (Map.Entry<String, BigDecimal> entry : coverages.entrySet()) {
            coverages.put(entry.getKey(), entry.getValue().setScale(2, RoundingMode.HALF_UP));
        }

        return coverages;
    }

    @Override
    public BigDecimal calculatePolicyPremium(AutoPolicy policy, Vehicle vehicle, Driver driver, UUID organizationId) {
        log.debug("Calcul de la prime pour la police: {}", policy.getPolicyNumber());

        // Calcul de la prime de base
        BigDecimal basePremium = calculateBasePremium(vehicle, policy.getCoverageType().name(), organizationId);

        // Calcul des facteurs
        Map<String, BigDecimal> factors = calculateFactors(vehicle, driver, policy.getCoverageType().name(), organizationId);

        // Calcul de la prime ajustée
        BigDecimal adjustedPremium = calculateAdjustedPremium(basePremium, factors);

        // Calcul de la prime finale avec le coefficient de bonus-malus
        BigDecimal finalPremium = calculateFinalPremium(adjustedPremium, policy.getBonusMalusCoefficient());

        return finalPremium;
    }
}
