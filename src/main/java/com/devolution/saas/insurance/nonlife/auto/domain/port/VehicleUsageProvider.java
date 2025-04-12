package com.devolution.saas.insurance.nonlife.auto.domain.port;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Port pour accéder aux types d'usage de véhicule depuis le domaine.
 */
public interface VehicleUsageProvider {

    /**
     * Obtient le facteur de risque associé à un type d'usage de véhicule.
     *
     * @param usageId L'ID du type d'usage
     * @return Le facteur de risque
     */
    BigDecimal getUsageRiskFactor(UUID usageId);
}
