package com.devolution.saas.insurance.nonlife.auto.infrastructure.adapter;

import com.devolution.saas.insurance.nonlife.auto.domain.port.VehicleUsageProvider;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleUsage;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Adaptateur qui implémente le port VehicleUsageProvider en utilisant le repository JPA.
 */
@Component
@RequiredArgsConstructor
public class VehicleUsageProviderAdapter implements VehicleUsageProvider {

    // Valeurs par défaut pour les facteurs de risque
    private static final BigDecimal DEFAULT_RISK_FACTOR = BigDecimal.ONE;
    private static final BigDecimal HIGH_RISK_FACTOR = new BigDecimal("1.3");
    private static final BigDecimal LOW_RISK_FACTOR = new BigDecimal("0.9");
    private final VehicleUsageRepository vehicleUsageRepository;

    @Override
    public BigDecimal getUsageRiskFactor(UUID usageId) {
        if (usageId == null) {
            return DEFAULT_RISK_FACTOR;
        }

        return vehicleUsageRepository.findById(usageId)
                .map(this::mapUsageToRiskFactor)
                .orElse(DEFAULT_RISK_FACTOR);
    }

    /**
     * Mappe un type d'usage de véhicule à un facteur de risque.
     * Cette méthode pourrait être améliorée pour utiliser des données réelles
     * stockées dans la base de données.
     *
     * @param usage Le type d'usage de véhicule
     * @return Le facteur de risque
     */
    private BigDecimal mapUsageToRiskFactor(VehicleUsage usage) {
        // Logique simplifiée - dans un système réel, ces valeurs seraient
        // probablement stockées dans la base de données
        String code = usage.getCode();
        if (code == null) {
            return DEFAULT_RISK_FACTOR;
        }

        if (code.contains("COMMERCIAL") || code.contains("TAXI")) {
            return HIGH_RISK_FACTOR;
        } else if (code.contains("OCCASIONAL") || code.contains("WEEKEND")) {
            return LOW_RISK_FACTOR;
        }

        return DEFAULT_RISK_FACTOR;
    }
}
