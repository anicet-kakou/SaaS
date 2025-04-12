package com.devolution.saas.insurance.nonlife.auto.infrastructure.adapter;

import com.devolution.saas.insurance.nonlife.auto.domain.port.VehicleCategoryProvider;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleCategory;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Adaptateur qui implémente le port VehicleCategoryProvider en utilisant le repository JPA.
 */
@Component
@RequiredArgsConstructor
public class VehicleCategoryProviderAdapter implements VehicleCategoryProvider {

    // Valeurs par défaut pour les facteurs de risque
    private static final BigDecimal DEFAULT_RISK_FACTOR = BigDecimal.ONE;
    private static final BigDecimal HIGH_RISK_FACTOR = new BigDecimal("1.5");
    private static final BigDecimal LOW_RISK_FACTOR = new BigDecimal("0.8");
    private final VehicleCategoryRepository vehicleCategoryRepository;

    @Override
    public BigDecimal getCategoryRiskFactor(UUID categoryId) {
        if (categoryId == null) {
            return DEFAULT_RISK_FACTOR;
        }

        return vehicleCategoryRepository.findById(categoryId)
                .map(this::mapCategoryToRiskFactor)
                .orElse(DEFAULT_RISK_FACTOR);
    }

    /**
     * Mappe une catégorie de véhicule à un facteur de risque.
     * Cette méthode pourrait être améliorée pour utiliser des données réelles
     * stockées dans la base de données.
     *
     * @param category La catégorie de véhicule
     * @return Le facteur de risque
     */
    private BigDecimal mapCategoryToRiskFactor(VehicleCategory category) {
        // Logique simplifiée - dans un système réel, ces valeurs seraient
        // probablement stockées dans la base de données
        String code = category.getCode();
        if (code == null) {
            return DEFAULT_RISK_FACTOR;
        }

        if (code.contains("SPORT") || code.contains("LUXURY")) {
            return HIGH_RISK_FACTOR;
        } else if (code.contains("COMPACT") || code.contains("ECONOMY")) {
            return LOW_RISK_FACTOR;
        }

        return DEFAULT_RISK_FACTOR;
    }
}
