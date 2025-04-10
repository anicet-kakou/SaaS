package com.devolution.saas.insurance.common.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Interface commune pour toutes les polices d'assurance.
 * Définit les méthodes que toutes les polices doivent implémenter.
 */
public interface Policy {

    /**
     * Retourne l'identifiant unique de la police.
     *
     * @return L'identifiant de la police
     */
    UUID getId();

    /**
     * Retourne le numéro de police.
     *
     * @return Le numéro de police
     */
    String getPolicyNumber();

    /**
     * Retourne le statut de la police.
     *
     * @return Le statut de la police
     */
    PolicyStatus getStatus();

    /**
     * Retourne la date de début de la police.
     *
     * @return La date de début
     */
    LocalDate getStartDate();

    /**
     * Retourne la date de fin de la police.
     *
     * @return La date de fin
     */
    LocalDate getEndDate();

    /**
     * Retourne le montant de la prime.
     *
     * @return Le montant de la prime
     */
    BigDecimal getPremiumAmount();

    /**
     * Retourne l'identifiant du produit associé à cette police.
     *
     * @return L'identifiant du produit
     */
    UUID getProductId();

    /**
     * Retourne l'identifiant du client.
     *
     * @return L'identifiant du client
     */
    UUID getCustomerId();

    /**
     * Retourne les garanties sélectionnées pour cette police.
     *
     * @return La liste des garanties sélectionnées
     */
    List<Coverage> getSelectedCoverages();

    /**
     * Retourne l'identifiant de l'organisation.
     *
     * @return L'identifiant de l'organisation
     */
    UUID getOrganizationId();

    /**
     * Calcule la prime pour cette police.
     *
     * @return Le montant de la prime
     */
    BigDecimal calculatePremium();

    /**
     * Renouvelle la police pour une nouvelle période.
     *
     * @param newStartDate La nouvelle date de début
     * @param newEndDate   La nouvelle date de fin
     * @return La nouvelle police
     */
    Policy renew(LocalDate newStartDate, LocalDate newEndDate);

    /**
     * Annule la police.
     *
     * @param cancellationDate La date d'annulation
     * @param reason           La raison de l'annulation
     * @return La police annulée
     */
    Policy cancel(LocalDate cancellationDate, String reason);

    /**
     * Statut d'une police d'assurance.
     */
    enum PolicyStatus {
        DRAFT,
        PENDING,
        ACTIVE,
        SUSPENDED,
        CANCELLED,
        EXPIRED
    }
}
