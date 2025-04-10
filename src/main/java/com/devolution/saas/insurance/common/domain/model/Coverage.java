package com.devolution.saas.insurance.common.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Interface commune pour toutes les garanties d'assurance.
 * Définit les méthodes que toutes les garanties doivent implémenter.
 */
public interface Coverage {

    /**
     * Retourne l'identifiant unique de la garantie.
     *
     * @return L'identifiant de la garantie
     */
    UUID getId();

    /**
     * Retourne le code unique de la garantie.
     *
     * @return Le code de la garantie
     */
    String getCode();

    /**
     * Retourne le nom de la garantie.
     *
     * @return Le nom de la garantie
     */
    String getName();

    /**
     * Retourne la description de la garantie.
     *
     * @return La description de la garantie
     */
    String getDescription();

    /**
     * Retourne le montant de la garantie.
     *
     * @return Le montant de la garantie
     */
    BigDecimal getAmount();

    /**
     * Retourne la franchise de la garantie.
     *
     * @return La franchise de la garantie
     */
    BigDecimal getDeductible();

    /**
     * Retourne le plafond de la garantie.
     *
     * @return Le plafond de la garantie
     */
    BigDecimal getCeiling();

    /**
     * Retourne l'identifiant du produit auquel cette garantie est associée.
     *
     * @return L'identifiant du produit
     */
    UUID getProductId();

    /**
     * Retourne l'identifiant de l'organisation.
     *
     * @return L'identifiant de l'organisation
     */
    UUID getOrganizationId();

    /**
     * Indique si cette garantie est incluse par défaut dans le produit.
     *
     * @return true si la garantie est incluse par défaut, false sinon
     */
    boolean isDefaultIncluded();

    /**
     * Indique si cette garantie est obligatoire.
     *
     * @return true si la garantie est obligatoire, false sinon
     */
    boolean isMandatory();

    /**
     * Calcule la prime pour cette garantie.
     *
     * @param context Le contexte de calcul
     * @return Le montant de la prime pour cette garantie
     */
    BigDecimal calculatePremium(InsuranceProduct.PolicyCalculationContext context);
}
