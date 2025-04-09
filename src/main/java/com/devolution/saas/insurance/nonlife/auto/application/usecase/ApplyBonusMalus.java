package com.devolution.saas.insurance.nonlife.auto.application.usecase;

import com.devolution.saas.insurance.nonlife.auto.application.dto.BonusMalusDTO;

import java.util.UUID;

/**
 * Cas d'utilisation pour l'application du bonus-malus.
 */
public interface ApplyBonusMalus {

    /**
     * Applique le bonus-malus pour un client.
     *
     * @param customerId     L'ID du client
     * @param claimCount     Le nombre de sinistres responsables
     * @param organizationId L'ID de l'organisation
     * @return Le bonus-malus mis à jour
     */
    BonusMalusDTO execute(UUID customerId, int claimCount, UUID organizationId);
}
