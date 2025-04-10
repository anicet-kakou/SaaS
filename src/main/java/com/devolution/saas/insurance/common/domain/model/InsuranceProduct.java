package com.devolution.saas.insurance.common.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Interface commune pour tous les produits d'assurance.
 * Définit les méthodes que tous les produits d'assurance doivent implémenter.
 */
public interface InsuranceProduct {

    /**
     * Retourne l'identifiant unique du produit.
     *
     * @return L'identifiant du produit
     */
    UUID getId();

    /**
     * Retourne le code unique du produit.
     *
     * @return Le code du produit
     */
    String getCode();

    /**
     * Retourne le nom du produit.
     *
     * @return Le nom du produit
     */
    String getName();

    /**
     * Retourne la description du produit.
     *
     * @return La description du produit
     */
    String getDescription();

    /**
     * Retourne le statut du produit.
     *
     * @return Le statut du produit
     */
    ProductStatus getStatus();

    /**
     * Retourne la date d'effet du produit.
     *
     * @return La date d'effet
     */
    LocalDate getEffectiveDate();

    /**
     * Retourne la date d'expiration du produit.
     *
     * @return La date d'expiration
     */
    LocalDate getExpiryDate();

    /**
     * Retourne l'identifiant de l'organisation.
     *
     * @return L'identifiant de l'organisation
     */
    UUID getOrganizationId();

    /**
     * Calcule la prime d'assurance pour ce produit.
     *
     * @param context Le contexte de calcul de la police
     * @return Le montant de la prime
     */
    BigDecimal calculatePremium(PolicyCalculationContext context);

    /**
     * Retourne les garanties disponibles pour ce produit.
     *
     * @return La liste des garanties disponibles
     */
    List<Coverage> getAvailableCoverages();

    /**
     * Valide la souscription à ce produit.
     *
     * @param context Le contexte de souscription
     * @return true si la souscription est valide, false sinon
     */
    boolean validateSubscription(SubscriptionContext context);

    /**
     * Statut d'un produit d'assurance.
     */
    enum ProductStatus {
        DRAFT,
        ACTIVE,
        INACTIVE,
        ARCHIVED
    }

    /**
     * Contexte de calcul de police.
     */
    interface PolicyCalculationContext {
        UUID getOrganizationId();
    }

    /**
     * Contexte de souscription.
     */
    interface SubscriptionContext {
        UUID getCustomerId();

        UUID getOrganizationId();
    }
}
