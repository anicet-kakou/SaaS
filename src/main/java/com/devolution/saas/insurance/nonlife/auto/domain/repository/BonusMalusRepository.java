package com.devolution.saas.insurance.nonlife.auto.domain.repository;

import com.devolution.saas.insurance.nonlife.auto.domain.model.BonusMalus;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour la gestion du bonus-malus.
 */
public interface BonusMalusRepository {

    /**
     * Sauvegarde un bonus-malus.
     *
     * @param bonusMalus Le bonus-malus à sauvegarder
     * @return Le bonus-malus sauvegardé
     */
    BonusMalus save(BonusMalus bonusMalus);

    /**
     * Trouve un bonus-malus par son ID.
     *
     * @param id L'ID du bonus-malus
     * @return Le bonus-malus trouvé, ou empty si non trouvé
     */
    Optional<BonusMalus> findById(UUID id);

    /**
     * Trouve le bonus-malus actif d'un client.
     *
     * @param customerId     L'ID du client
     * @param organizationId L'ID de l'organisation
     * @return Le bonus-malus trouvé, ou empty si non trouvé
     */
    Optional<BonusMalus> findActiveByCustomerId(UUID customerId, UUID organizationId);

    /**
     * Supprime un bonus-malus.
     *
     * @param id L'ID du bonus-malus à supprimer
     */
    void deleteById(UUID id);
}
