package com.devolution.saas.insurance.common.domain.service;

import com.devolution.saas.insurance.common.domain.model.Coverage;
import com.devolution.saas.insurance.common.domain.model.InsuranceProduct;
import com.devolution.saas.insurance.common.domain.model.Policy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Interface commune pour le calcul de prime d'assurance.
 * Définit les méthodes que tous les calculateurs de prime doivent implémenter.
 */
public interface PremiumCalculator {

    /**
     * Calcule la prime de base pour un produit.
     *
     * @param product        Le produit d'assurance
     * @param context        Le contexte de calcul
     * @param organizationId L'ID de l'organisation
     * @return La prime de base
     */
    BigDecimal calculateBasePremium(InsuranceProduct product, InsuranceProduct.PolicyCalculationContext context, UUID organizationId);

    /**
     * Calcule les facteurs d'ajustement pour une police.
     *
     * @param context        Le contexte de calcul
     * @param organizationId L'ID de l'organisation
     * @return Les facteurs d'ajustement
     */
    Map<String, BigDecimal> calculateFactors(InsuranceProduct.PolicyCalculationContext context, UUID organizationId);

    /**
     * Calcule la prime ajustée en appliquant les facteurs à la prime de base.
     *
     * @param basePremium La prime de base
     * @param factors     Les facteurs d'ajustement
     * @return La prime ajustée
     */
    BigDecimal calculateAdjustedPremium(BigDecimal basePremium, Map<String, BigDecimal> factors);

    /**
     * Calcule la prime pour chaque garantie.
     *
     * @param coverages      Les garanties
     * @param context        Le contexte de calcul
     * @param organizationId L'ID de l'organisation
     * @return Les primes par garantie
     */
    Map<String, BigDecimal> calculateCoveragePremiums(List<Coverage> coverages, InsuranceProduct.PolicyCalculationContext context, UUID organizationId);

    /**
     * Calcule la prime finale pour une police.
     *
     * @param policy         La police
     * @param organizationId L'ID de l'organisation
     * @return La prime finale
     */
    BigDecimal calculatePolicyPremium(Policy policy, UUID organizationId);
}
