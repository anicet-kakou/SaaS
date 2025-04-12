package com.devolution.saas.insurance.nonlife.auto.domain.port;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Port pour accéder aux catégories de véhicule depuis le domaine.
 */
public interface VehicleCategoryProvider {

    /**
     * Obtient le facteur de risque associé à une catégorie de véhicule.
     *
     * @param categoryId L'ID de la catégorie
     * @return Le facteur de risque
     */
    BigDecimal getCategoryRiskFactor(UUID categoryId);
}
